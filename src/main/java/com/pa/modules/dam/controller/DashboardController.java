package com.pa.modules.dam.controller;

import com.pa.commons.Routes;
import com.pa.config.JsonResponseBodyTemplate;
import com.pa.modules.dam.model.Dashboard;
import com.pa.modules.dam.service.DamService;
import com.pa.modules.dam.service.DamdariService;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(produces = "application/json")

public class DashboardController {
    private UserService userService;

    private DamdariService damdariService;

    private DamService  damService;

    @Autowired
    public DashboardController(UserService userService, DamdariService damdariService, DamService damService) {
        this.userService = userService;
        this.damdariService = damdariService;
        this.damService = damService;
    }
    @GetMapping(value = {Routes.Get_dashboard,Routes.Get_dashboard_damdariId})
    public ResponseEntity<Object> getDashboard(
            @PathVariable(required = false) String damdariId,
            HttpServletResponse response) {
        try {
           Dashboard dashboard = damService.getDashboardData(damdariId);
            return ResponseEntity.ok()
                    .body(dashboard);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
