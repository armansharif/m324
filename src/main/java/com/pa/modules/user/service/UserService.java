package com.pa.modules.user.service;


import com.google.common.collect.ImmutableMap;
import com.pa.commons.CommonUtils;
import com.pa.commons.exception.UserServiceException;
import com.pa.enums.Authority;
import com.pa.modules.committee.consts.ConstCommittee;
import com.pa.modules.committee.model.MembersDTO;
import com.pa.modules.convert.ConvertEnFa;
import com.pa.modules.jwt.JwtUtils;

import com.pa.modules.location.model.District;
import com.pa.modules.location.service.LocationService;
import com.pa.modules.mail.SendMail;
import com.pa.modules.sms.SmsVerification;
import com.pa.modules.user.model.Roles;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.repository.RolesRepository;
import com.pa.modules.user.repository.UsersRepository;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final SmsVerification smsVerification;

    private final RolesRepository rolesRepository;
    private UsersRepository usersRepository;

    private LocationService locationService;
    private Environment env;

    @Autowired
    public UserService(UsersRepository usersRepository, ConvertEnFa convertEnFa, SmsVerification smsVerification, RolesRepository rolesRepository, LocationService locationService, Environment env) {
        this.usersRepository = usersRepository;
        this.smsVerification = smsVerification;
        this.rolesRepository = rolesRepository;
        this.locationService = locationService;
        this.env = env;
    }

    @Value("${general.user.registrationOnFirstLogin.enable}")
    private boolean registrationOnFirstLogin;


    public String generateCode(Users users) {
        return "code";
        //   return "code" + users.getMobile().substring((users.getMobile().length() - 4), users.getMobile().length());
    }

    public String verificationUser(String mobile) {
        env.getProperty("conf.urlUserImage");

        int statusUser = 0; //0 == new common user , 1 == old common user , 2  = admin user
        String statusUserStr = "کاربر جدید در سامانه ثبت شد";
        String response = "";
        try {
            String smsCode = smsVerification.generateCode() + "";
            Users user = usersRepository.findByMobile(mobile);
            if (user != null) {
                user.setPassword(smsCode);
                this.usersRepository.save(user);

                if (user.getRoles()
                        .stream()
                        .filter(r -> r.getAuthorities().contains(Authority.IS_ADMIN))
                        .collect(Collectors.toList()).isEmpty()) {
                    statusUserStr = "کاربر پیش از این در سامانه ثبت نام کرده است";
                    statusUser = 1;
                } else {
                    statusUserStr = "مدیر پیش از این در سامانه ثبت نام شده است";
                    statusUser = 2;
                }
            } else if (registrationOnFirstLogin) {
                Set<Roles> roles = new HashSet<>();
                roles.add(rolesRepository.findRolesByName("user"));
                user = new Users(mobile, smsCode, roles);
                user.setHeadCode(generateCode(user));
                //register new user
                this.usersRepository.save(user);
            } else {
                JSONObject resJson = new JSONObject();
                resJson.put("code", 200);
                resJson.put("status", "fail");
                resJson.put("message", "User not found");
                return resJson.toString();
            }
            if (user != null) {
                //  response = smsVerification.sendSmsVerificationGhasedak(mobile, smsCode);
                JSONObject r = smsVerification.sendSmsVerificationSMSIR(mobile, smsCode);
                r.put("userStatue", statusUser);
                r.put("userStatueStr", statusUserStr);
                response = r.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String verificationUserCM(String mobile) {
        String response = "";
        JSONObject resJson = new JSONObject();
        try {
            String idOfCM = smsVerification.sendSmsGenerateCMCOM(mobile);
            Users user = usersRepository.findByMobile(mobile);
            if (user != null) {
                user.setPassword(idOfCM);
                this.usersRepository.save(user);

            } else {
                Set<Roles> roles = new HashSet<>();
                roles.add(rolesRepository.findRolesByName("user"));
                user = new Users(mobile, idOfCM, roles);
                user.setHeadCode(generateCode(user));
                user.setEmail(UUID.randomUUID().toString());
                //register new user
                this.usersRepository.save(user);
            }

            resJson.put("code", 200);
            resJson.put("status", "success");
            resJson.put("message", "Verification code sent successfully.");
        } catch (IOException e) {
            resJson.put("code", 401);
            resJson.put("status", "fail");
            resJson.put("message", "Unfortunately, there is a problem");
            e.printStackTrace();
        }
        return resJson.toString();
    }

    public int verificationUserByEmail(String email) {
        SendMail sendMail = new SendMail();
        int response = -1;
        try {
            String smsCode = smsVerification.generateCode() + "";
            Users user = usersRepository.findByEmail(email);
            if (user != null) {
                user.setPassword(smsCode);
                this.usersRepository.save(user);

            } else {
                Set<Roles> roles = new HashSet<>();
                roles.add(rolesRepository.findRolesByName("user"));
                user = new Users("", email, smsCode, roles);
                user.setHeadCode(generateCode(user));
                user.setMobile(UUID.randomUUID().toString());
                //register new user
                this.usersRepository.save(user);
            }
            response = sendMail.sendMail(email, " Verification Code : ", " <p> <h3>    " + smsCode + "</h3></p>");
            //  response =   sendMail.asycSendMailNewUser(email,  " asycSendMailNewUser Verification Code ", " <p> <h3>    " + smsCode + "</h3></p>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Users registerUser(Users users) throws IOException {
        String urlPostImageString = env.getProperty("conf.urlUserImage");

        String rootUserImageString = env.getProperty("conf.rootUserImage");
        Path rootUsersImage = Paths.get(rootUserImageString);
        String path = rootUsersImage.toFile().getAbsolutePath();


        if (users.getFile() != null && users.getFile().getSize() > 0) {
            byte[] bytes = users.getFile().getBytes();
            String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(users.getFile().getContentType()).split("/")[1];
            Files.write(Paths.get(path + File.separator + fileName), bytes);
            users.setImg(urlPostImageString + fileName);
        }
        users.setHeadCode(generateCode(users));
        return this.usersRepository.save(users);
    }

    public Users updateUser(Long id,
                            String name,
                            String family,
                            String email,
                            String address,
                            MultipartFile file) throws IOException {
        Optional<Users> user = usersRepository.findById(id);
        String urlPostImageString = env.getProperty("conf.urlUserImage");
        String rootUserImageString = env.getProperty("conf.rootUserImage");
        Path rootUsersImage = Paths.get(rootUserImageString);
        if (user.isPresent()) {
            user.get().setName(name);
            user.get().setFamily(family);
            user.get().setEmail(email);
            user.get().setAddress(address);
            String path = rootUsersImage.toFile().getAbsolutePath();
            if (file != null) {
                byte[] bytes = file.getBytes();
                String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
                Files.write(Paths.get(path + File.separator + fileName), bytes);
                user.get().setImg(urlPostImageString + fileName);
            }
        }
        return this.usersRepository.save(user.orElse(null));
    }


    public Users updateUser(Long id,
                            String name,
                            String family,
                            String email,
                            String mobile,
                            String username,
                            String password,
                            String address,
                            MultipartFile file) throws IOException {
        return updateUser(id,
                name,
                family,
                email,
                mobile,
                username,
                password,
                address,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                file);
    }

    public Users updateUser(Long id,
                            String name,
                            String family,
                            String email,
                            String mobile,
                            String username,
                            String password,
                            String address,
                            Long education,
                            Users refUser,
                            String expertise,
                            Long districtId,
                            Long university,
                            String refCode,
                            Long reasonSelectCommittee,
                            Integer facultyMembership,
                            Integer eliteMembership,
                            Integer gpa,
                            Integer authoredBook,
                            Integer translatedBook,
                            Integer articles,
                            Integer workExperience,
                            String headCode,
                            MultipartFile file) throws IOException {
        Optional<Users> user = usersRepository.findById(id);
        String urlPostImageString = env.getProperty("conf.urlUserImage");
        String rootUserImageString = env.getProperty("conf.rootUserImage");
        Path rootUsersImage = Paths.get(rootUserImageString);
        if (user.isPresent()) {
            if (name != null)
                user.get().setName(name);
            if (family != null)
                user.get().setFamily(family);
            if (email != null)
                user.get().setEmail(email);
            if (mobile != null)
                user.get().setMobile(mobile);
            if (username != null)
                user.get().setUsername(username);
            if (password != null)
                user.get().setAdminPassword(password);
            if (address != null)
                user.get().setAddress(address);
            if (refUser != null)
                user.get().setRefUser(refUser);
            if (education != null)
                user.get().setEducation(education);
            if (expertise != null)
                user.get().setExpertise(expertise);
            if (refCode != null)
                user.get().setRefCode(refCode);
            if (university != null)
                user.get().setUniversity(university);
            if (districtId != null)
                user.get().setDistrict(new District(districtId));
            if (reasonSelectCommittee != null)
                user.get().setReasonSelectCommittee(reasonSelectCommittee);
            if (facultyMembership != null)
                user.get().setFacultyMembership(facultyMembership);
            if (eliteMembership != null)
                user.get().setEliteMembership(eliteMembership);
            if (gpa != null)
                user.get().setGpa(gpa);
            if (authoredBook != null)
                user.get().setAuthoredBook(authoredBook);
            if (translatedBook != null)
                user.get().setTranslatedBook(translatedBook);
            if (articles != null)
                user.get().setArticles(articles);
            if (workExperience != null)
                user.get().setWorkExperience(workExperience);
            if (headCode != null)
                user.get().setHeadCode(headCode);

            String path = rootUsersImage.toFile().getAbsolutePath();
            if (file != null) {
                byte[] bytes = file.getBytes();
                String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
                Files.write(Paths.get(path + File.separator + fileName), bytes);
                user.get().setImg(urlPostImageString + fileName);
            }
        }
        Users user_saved = null;
        try {
            user_saved = this.usersRepository.save(user.orElse(null));
            setBaseScoreOfUser(user_saved);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return this.usersRepository.save(user.orElse(null));
    }

    public String generateAutoIncremantString(Long id, int length) {
        final Map<Integer, String> digitToCharMap = ImmutableMap.of(
                0, "A",
                1, "I",
                2, "T",
                3, "E",
                4, "M",
                5, "S",
                6, "G",
                7, "V",
                8, "B",
                9, "P"
        );

        String paddedStringId = StringUtils.leftPad(Long.toString(id), length, "0");
        for (Map.Entry<Integer, String> entry : digitToCharMap.entrySet()) {
            paddedStringId = paddedStringId.replaceAll(entry.getKey().toString(), entry.getValue());
        }
        return paddedStringId;
    }

    public String generateCodeForUser(Long edu, Long uni, Long districtId, Users users) {


        final Map<Integer, String> universityToCharMap = ImmutableMap.of(
                0, "O",
                1, "A",
                2, "G"
        );

        final Map<Integer, String> educationToCharMap = ImmutableMap.of(
                0, "O",
                1, "B",
                2, "M",
                3, "P"
        );

        String paddedStringId = generateAutoIncremantString(users.getId(), 5);
        StringBuffer code = new StringBuffer();

        districtId = districtId == null ? users.getDistrict().getId() : districtId;

        District district = null;
        if (CommonUtils.isNull(districtId) || CommonUtils.isNull(edu) || CommonUtils.isNull(uni)) {
            code.append("EDU")
                    .append("UNI")
                    .append("STT")
                    .append("ELEDIS")
                    .append("DIS")
                    .append(324)
                    .append(paddedStringId);
        } else {
            district = locationService.findDistrictById(districtId);
            code.append(educationToCharMap.get(edu.intValue()))
                    .append(educationToCharMap.get(uni.intValue()))
                    .append(district.getCity().getState() == null ? "STT" : district.getCity().getState().getCode())
                    .append(district.getElectoralDistricts() == null ? "ELEDIS" : district.getElectoralDistricts().getCode())
                    .append(district.getCode() == null ? "DIS" : district.getCode())
                    .append(324)
                    .append(paddedStringId);
        }


        return code.toString().toUpperCase();
    }

    public List<Users> findAllUsers() {
        return this.usersRepository.findAll();
    }

    public List<Users> findAllUsers(int page, int perPage, String sort, Specification<Users> userSpec) {
        Pageable postSortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());
        Page<Users> pagedResult = this.usersRepository.findAll(userSpec, postSortedAndPagination);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Users>();
        }
    }


    public Optional<Users> findUser(Long id) {
        return this.usersRepository.findById(id);
    }

    public Users findUserByMobile(String mobile) {
//        if (userEntity == null) {
//            throw new UserServiceException("User " + username + " not found");
//        }

        return this.usersRepository.findByMobile(mobile);
    }

    public Users findUserByRefCode(String refCode) {
        return this.usersRepository.findByRefCode(refCode);
    }

    public Set<MembersDTO> findPresentedUsers(Users user) {
        Set<MembersDTO> presentedUser = new HashSet<>();
        Set<Users> presentedUsers = this.usersRepository.findAllByRefUser(user);
        for (Users u : presentedUsers) {
            MembersDTO member = new MembersDTO();
            if (u.getName() != null)
                member.setFullName(u.getName());
            member.setUserId(u.getId());
            presentedUser.add(member);
        }
        return presentedUser;
    }


    public Users findUserByUserName(String username) {
        return this.usersRepository.findByUsername(username);
    }

    public Users findUserByEmail(String email) {
        return this.usersRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username);

    }

    public Optional<Users> checkVerificationUser(String mobile, String code) {
        return usersRepository.validateVerificationCode(mobile, code);
    }

    public boolean checkVerificationUserCMCOM(Users user, String code) throws IOException {
        return smsVerification.verificationCMCOM(user.getPassword(), code);
    }

    public Users checkVerificationUser(String mobile, String email, String code) {
        return usersRepository.validateVerificationCode(mobile, email, code);
    }

    public Users checkVerificationUserByEmail(String email, String code) {
        return usersRepository.validateVerificationCodeByEmail(email, code);
    }

    public Users checkVerificationAdmin(String mobile, String pass) {
        return usersRepository.findByMobileAndAdminPasswordAndAdminPasswordIsNotNull(mobile, pass);
    }

    public Users login(String username, String pass) {
        return usersRepository.findByUsernameAndAdminPasswordAndAdminPasswordIsNotNull(username, pass);
    }


    public void deleteUser(Long id) throws Exception {
        this.usersRepository.deleteById(id);
    }

    public boolean checkUserAndToken(Long userId, HttpServletRequest request) {
        JwtUtils jwtUtils = new JwtUtils();
        Long userIdByToken = jwtUtils.getUserId(request);
        //check token is valid
        Optional<Users> user = findUser(userId);
        if (user.isPresent() && userId == userIdByToken) {
            return true;
        } else {
            return false;
        }
    }

    public Long getUserIdByToken(HttpServletRequest request) {
        JwtUtils jwtUtils = new JwtUtils();
        return jwtUtils.getUserId(request);
    }

    public Users getUserByToken(HttpServletRequest request) {
        JwtUtils jwtUtils = new JwtUtils();
        Users user = findUser(getUserIdByToken(request)).orElse(null);
        if (user == null)
            throw new UserServiceException("User not found");
        return user;
    }

    public void setBaseScoreOfUser(Users user) {

        int score = ConstCommittee.SCORE_USER_EDUCATION.get(user.getEducation().intValue())
                + ConstCommittee.SCORE_USER_UNIVERSITY.get(user.getUniversity().intValue())
                + ConstCommittee.SCORE_USER_REASON.get(user.getReasonSelectCommittee().intValue())
                + ConstCommittee.SCORE_USER_REASON.get(user.getReasonSelectCommittee().intValue())
                + user.getFacultyMembership() == 1L ? ConstCommittee.SCORE_USER_FACILITY_MEMBERSHIP : 0
                + user.getEliteMembership() == 1L ? ConstCommittee.SCORE_USER_ELITE_MEMBERSHIP : 0
                + ConstCommittee.SCORE_USER_GPA.get(user.getGpa())
                + (user.getAuthoredBook() * ConstCommittee.SCORE_USER_PER_AUTHORED_BOOK)
                + (user.getTranslatedBook() * ConstCommittee.SCORE_USER_PER_TRANSLATED_BOOK)
                + (user.getArticles() * ConstCommittee.SCORE_USER_PER_ARTICLE)
                + (user.getWorkExperience() * ConstCommittee.SCORE_USER_PER_WORK_EXPERIENCE_YEAR);

        user.setBaseScore(score);
        usersRepository.save(user);
    }
}
