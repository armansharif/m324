package com.pa.modules.committee.service;

import com.pa.commons.CommonUtils;
import com.pa.commons.exception.UserServiceException;
import com.pa.modules.committee.consts.ConstCommittee;
import com.pa.modules.committee.model.Committee;
import com.pa.modules.committee.model.MembersDTO;
import com.pa.modules.committee.model.MembershipRequest;
import com.pa.modules.committee.repository.CommitteeRepository;
import com.pa.modules.committee.repository.MembershipRequestRepository;
import com.pa.modules.notification.repository.NotificationRepository;
import com.pa.modules.notification.model.Notification;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.repository.UsersRepository;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
public class CommitteeService {

    private CommitteeRepository committeeRepository;
    private UserService userService;
    private UsersRepository usersRepository;
    private NotificationRepository notificationRepository;
    private MembershipRequestRepository membershipRequestRepository;

    @Autowired
    public CommitteeService(CommitteeRepository committeeRepository, UserService userService, UsersRepository usersRepository, NotificationRepository notificationRepository, MembershipRequestRepository membershipRequestRepository) {
        this.committeeRepository = committeeRepository;
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.notificationRepository = notificationRepository;
        this.membershipRequestRepository = membershipRequestRepository;
    }

    public List<Committee> findAllCommittee() {
        List<Committee> committees = committeeRepository.getAllCommissions();
        for (Committee c : committees) {
            Set<MembersDTO> members = new HashSet<>();
            Set<Users> users = c.getUsers();
            for (Users u : users) {
                MembersDTO member = new MembersDTO();
                if (u.getName() != null)
                    member.setFullName(u.getName());
                member.setUserId(u.getId());
                members.add(member);
            }
            c.setMembers(members);
        }
        return committees;
    }

    public List<Committee> findAllCommittee(int page, int perPage, String sort) {
        Pageable sortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());
        Page<Committee> pagedResult = committeeRepository.findAllByIsCommission(1, sortedAndPagination);
        if (pagedResult.hasContent()) {
            List<Committee> committees = pagedResult.getContent();
            for (Committee c : committees) {
                Set<MembersDTO> members = new HashSet<>();
                Set<Users> users = c.getUsers();
                for (Users u : users) {
                    MembersDTO member = new MembersDTO();
                    if (u.getName() != null)
                        member.setFullName(u.getName());
                    member.setUserId(u.getId());
                    members.add(member);
                }
                c.setMembers(members);
            }
            return committees;
        } else {
            return new ArrayList<Committee>();
        }

    }

    public Map<String, String> postCommitteeRequest(Long userId,
                                                    Long committeeId,
                                                    String description) {
        Map<String, String> res = new HashMap<>();
        Users user = userService.findUser(userId).orElse(null);
        if (user == null) {
            throw new UserServiceException("User not found");
        }

        Committee committee = committeeRepository.findById(committeeId).orElse(null);

        if (committee == null) {
            throw new UserServiceException("Committee not found");
        }

        if (committee.getIsCommission() == 1) {
            throw new UserServiceException("can not Request on commission");
        }

        MembershipRequest checkExistMembershipRequest = membershipRequestRepository.findByCommitteeAndUser(committee, user);
        description = " درخواست  کاربر  " + user.getName() + "  برای  عضویت در " + committee.getName() + " ثبت شده است.";

        if (CommonUtils.isNotNull(checkExistMembershipRequest)) {
            throw new UserServiceException(" این درخواست  قبلا برای این کاربر ثبت شده است");
        } else {
            MembershipRequest membershipRequest = new MembershipRequest();
            membershipRequest.setUser(user);
            membershipRequest.setCommittee(committee);
            membershipRequest.setDescription(description);
            membershipRequest.setStatus(ConstCommittee.COMMITTEE_STATUS_DRAFT);
            membershipRequestRepository.save(membershipRequest);
        }
        res.put("user", user.getName());
        res.put("committee", committee.getName());
        res.put("result", " درخواست عضویت با موفقیت ثبت شد");

        Set<Users> adminOfCommission = committee.getParent().getUsers();
        Iterator<Users> adminOfCommissionIterator = adminOfCommission.iterator();
        while (adminOfCommissionIterator.hasNext()) {
            Notification notification = new Notification();
            notification.setRoute("committee/request");
            notification.setMessage(description);
            notification.setIsRead(0);
            notification.setReadOnly(0);
            notification.setUsers(adminOfCommissionIterator.next());
            notificationRepository.save(notification);
        }

        Notification notification = new Notification();
        notification.setRoute("committeeRequest");
        notification.setMessage(description);
        notification.setIsRead(0);
        notification.setReadOnly(1);
        notification.setUsers(user);
        notificationRepository.save(notification);
        return res;
    }

    public List<MembershipRequest> getMembershipRequests(Users user) {
        List<MembershipRequest> result = new ArrayList<>();
        List<MembershipRequest> membershipRequests = membershipRequestRepository.getAllMembershipRequestForOwner(user.getId());

        for (MembershipRequest membershipRequest : membershipRequests) {
            if (membershipRequest.getUser().getId() == user.getId()) {
                membershipRequest.setIsAcceptable(0);
                membershipRequest.setIsRejectable(0);
            } else {
                membershipRequest.setIsAcceptable(1);
                membershipRequest.setIsRejectable(1);
            }
            result.add(membershipRequest);
        }
        return result;
    }


    public Optional<Committee> findCommittee(Long id) {
        return this.committeeRepository.findById(id);
    }

    public Optional<MembershipRequest> findMembershipRequest(Long id) {
        return this.membershipRequestRepository.findById(id);
    }

    public Committee setCommitteeRequestAcceptReject(Long requestId, HttpServletRequest request, boolean isAccept) {


        if (membershipRequestRepository.getAllMembershipRequestForOwner(userService.getUserIdByToken(request)).isEmpty()) {
            throw new UserServiceException("Invalid request ");
        }

        MembershipRequest membershipRequest = findMembershipRequest(requestId).orElse(null);

        if (membershipRequest == null) {
            throw new UserServiceException("Request not found");
        }


        Users user = userService.findUser(membershipRequest.getUser().getId()).orElse(null);
        if (user == null) {
            throw new UserServiceException("User not found");
        }

        Committee committee = findCommittee(membershipRequest.getCommittee().getId()).orElse(null);
        if (user == null) {
            throw new UserServiceException("committee not found");
        }
        committee.getUsers().add(user);
        committeeRepository.save(committee);

        user.getCommittee().add(committee);
        usersRepository.saveAndFlush(user);

        Notification notification = new Notification();
        notification.setUsers(user);
        notification.setReadOnly(1);
        notification.setRoute("none");

        if (isAccept) {
            membershipRequest.setStatus(ConstCommittee.COMMITTEE_STATUS_ACCEPT);
            notification.setMessage("با درخواست عضویت شما در " + committee.getName() + " موافقت شد.");
            Notification afterAcceptNotification = new Notification();
            afterAcceptNotification.setUsers(user);
            afterAcceptNotification.setReadOnly(0);
            afterAcceptNotification.setRoute("completeProfile");
            afterAcceptNotification.setMessage(" با توجه به پذیرفته شدن شما در کمیته تخصصی در صورت تمایل فرم اطلاعات تخصصی را تکمیل فرمایید");
            notificationRepository.save(afterAcceptNotification);
        } else {
            membershipRequest.setStatus(ConstCommittee.COMMITTEE_STATUS_REJECT);
            notification.setMessage("متاسفانه با درخواست عضویت شما در " + committee.getName() + " موافقت نشد.");
        }
        membershipRequestRepository.save(membershipRequest);
        notificationRepository.save(notification);

        return committee;
    }
}