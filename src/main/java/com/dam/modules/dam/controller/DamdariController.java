package com.dam.modules.dam.controller;

import com.dam.commons.Routes;
import com.dam.config.JsonResponseBodyTemplate;
import com.dam.modules.dam.model.Dam;
import com.dam.modules.dam.model.Damdari;
import com.dam.modules.dam.service.DamdariService;
import com.dam.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json")

public class DamdariController {

    private UserService userService;

    private DamdariService damdariService;

    @Autowired
    public DamdariController(UserService userService, DamdariService damdariService) {
        this.userService = userService;
        this.damdariService = damdariService;
    }

    @GetMapping(value = {Routes.Get_damdari})
    public ResponseEntity<Object> findDamdaries(
            @PathVariable(required = false) String damdariId,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int perPage,
            HttpServletResponse response) {
        try {
            List<Damdari> DamdariList = this.damdariService.findAllDamdari(sort, page, perPage);
            return ResponseEntity.ok()
                    .body(DamdariList);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
