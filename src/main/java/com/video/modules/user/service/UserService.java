package com.video.modules.user.service;


import com.video.modules.convert.ConvertEnFa;
import com.video.modules.jwt.JwtUtils;
import com.video.modules.sms.SmsVerification;
import com.video.modules.user.model.Roles;
import com.video.modules.user.model.Users;
import com.video.modules.user.repository.RolesRepository;
import com.video.modules.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class UserService implements UserDetailsService {

    private final Path rootUsersImage = Paths.get("webapps/uploads/img/users");
    private final SmsVerification smsVerification;

    private final RolesRepository rolesRepository;
    private UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository, ConvertEnFa convertEnFa, SmsVerification smsVerification, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.smsVerification = smsVerification;
        this.rolesRepository = rolesRepository;
    }

    //validation is here
    public String generateCode(Users users) {
        return "MG" + users.getId() + users.getMobile().substring((users.getMobile().length() - 4), users.getMobile().length());
    }

    public String verificationUser(String mobile) {
        String response = "";
        try {
            String smsCode = smsVerification.generateCode() + "";
            Users user = usersRepository.findByMobile(mobile);
            if (user != null) {
                user.setPassword(smsCode);
                this.usersRepository.save(user);

            } else {
                Set<Roles> roles = new HashSet<>();
                roles.add(rolesRepository.findRolesByName("user"));
                user = new Users(mobile, smsCode, roles);
                user.setCode(generateCode(user));
                //register new user
                this.usersRepository.save(user);
            }
            response = smsVerification.sendSmsVerification(mobile, smsCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Users registerUser(Users users) throws IOException {
        String path = rootUsersImage.toFile().getAbsolutePath();
        if (users.getFile() != null) {
            byte[] bytes = users.getFile().getBytes();
            String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(users.getFile().getContentType()).split("/")[1];
            Files.write(Paths.get(path + File.separator + fileName), bytes);
            users.setImg(fileName);
        }
        users.setCode(generateCode(users));
        return this.usersRepository.save(users);
    }

    public Users updateUser(Long id,
                            String name,
                            String family,
                            String email,
                            String address,
                            MultipartFile file) throws IOException {
        Optional<Users> user = usersRepository.findById(id);
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
                user.get().setImg(fileName);
            }
        }
        return this.usersRepository.save(user.orElse(null));
    }

    public List<Users> findAllUsers() {
        return this.usersRepository.findAll();
    }

    public Optional<Users> findUser(Long id) {
        return this.usersRepository.findById(id);


    }

    public Users findUserByMobile(String mobile) {
        return this.usersRepository.findByMobile(mobile);
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        return usersRepository.findByMobile(mobile);

    }

    public Users checkVerificationUser(String mobile, String code) {
        return usersRepository.validateVerificationCode(mobile, code);
    }
    public void deleteUser(Long id) throws Exception{
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

}
