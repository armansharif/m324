package com.pa.modules.committee.controller;

import com.pa.commons.Routes;
import com.pa.modules.committee.model.Committee;
import com.pa.modules.committee.service.CommitteeService;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = "application/json")
public class CommitteeController {
    private CommitteeService committeeService;
    private UserService userService;

    @Autowired
    public CommitteeController(CommitteeService committeeService, UserService userService) {
        this.committeeService = committeeService;
        this.userService = userService;
    }

    @GetMapping(value = {Routes.GET_committee})
    public List<Committee> getAllCommittee(HttpServletResponse response) {
        return committeeService.findAllCommittee();
    }


    @PostMapping(value = {Routes.POST_committee_request})
    public Map<String, String> postCommitteeRequest(HttpServletResponse response,
                                                    @RequestParam Long userId,
                                                    @RequestParam Long committeeId,
                                                    @RequestParam(required = false) String description
    ) {
        return committeeService.postCommitteeRequest(userId, committeeId, description);
    }

    @GetMapping(value = {Routes.GET_committee_requests})
    // @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    public ResponseEntity<Object> getCommitteeRequests(HttpServletRequest request) {
        Long user_id = userService.getUserIdByToken(request);
        if (user_id == null)
            return ResponseEntity.notFound().build();
        Users user = userService.findUser(user_id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok()
                    .body(committeeService.getMembershipRequests(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping(value = {Routes.POST_committee_request_accept})
    public Committee setCommitteeRequestAccept(HttpServletResponse response, HttpServletRequest request,
                                               @RequestParam Long requestId
    ) {

        return committeeService.setCommitteeRequestAcceptReject(requestId, request,true);
    }

    @PostMapping(value = {Routes.POST_committee_request_reject})
    public Committee setCommitteeRequestReject(HttpServletResponse response, HttpServletRequest request,
                                               @RequestParam Long requestId
    ) {

        return committeeService.setCommitteeRequestAcceptReject(requestId, request,false);
    }

}
