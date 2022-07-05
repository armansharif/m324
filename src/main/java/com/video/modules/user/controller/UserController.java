package com.video.modules.user.controller;

import com.video.config.JsonResponseBodyTemplate;
import com.video.modules.jwt.JwtUtils;
import com.video.modules.user.model.Addresses;
import com.video.modules.user.model.Users;
import com.video.modules.user.service.AddressesService;
import com.video.modules.user.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AddressesService addressesService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, AddressesService addressesService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.addressesService = addressesService;
    }

    @GetMapping(value = {"/users", "/users/"})
    @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    public List<Users> getUsers() {
        logger.info("try to get user list.");
        return userService.findAllUsers();
    }

    @GetMapping(value = {"/users/{id}"})
    @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    public ResponseEntity<Object> getUser(@PathVariable(value = "id") Long id) {
        Users user = userService.findUser(id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok()
                    .body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(value = {"/auth", "/auth/"}, produces = "application/json")
    public String verificationUser(@RequestParam String mobile) {
        logger.info("try to verification.");
        return userService.verificationUser(mobile);
    }


    @GetMapping(value = {"/users/{id}/addresses", "/users/{id}/addresses/"})
    @PreAuthorize("hasAuthority('OP_ACCESS_PUBLIC')")
    public ResponseEntity<Object> getAddressOfUser(@PathVariable(value = "id") Long id) {
        try {

            //check token is valid
            Optional<Users> user = this.userService.findUser(id);
            if (user.isPresent()) {
                return ResponseEntity.ok()
                        .body(this.addressesService.findAddressOfUser(user.get()));
            } else {
                return new ResponseEntity<>(
                        JsonResponseBodyTemplate.
                                createResponseJson("fail", HttpStatus.FORBIDDEN.value(), "کاربر معتبر نیست").toString(),
                        HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping(value = {"/auth", "/auth/"}, produces = "application/json")
    public ResponseEntity<?> verificationUser(@RequestParam String mobile, @RequestParam String code, HttpServletResponse response) {
        Users user = userService.checkVerificationUser(mobile, code);
        if (user == null) {
            logger.info(" verification failed");
            JSONObject unSuccessfulLogin = new JSONObject();
            unSuccessfulLogin.put("code", response.getStatus());
            unSuccessfulLogin.put("token", "");
            unSuccessfulLogin.put("status", "fail");
            unSuccessfulLogin.put("message", "کد تایید منقضی شده و یا اشتباه وارد شده است. ");
            return ResponseEntity.badRequest().body(unSuccessfulLogin.toString());
        }
        String token = jwtUtils.generateToken(mobile, user.getId());
        response.addHeader("Authorization", token);
        JSONObject successfulLogin = new JSONObject();
        successfulLogin.put("code", response.getStatus());
        successfulLogin.put("token", token);
        successfulLogin.put("status", "success");
        successfulLogin.put("message", "توکن با موفقیت ساخته شد. ");
        logger.info(" verification successful");
        return ResponseEntity.ok()
                .body(successfulLogin.toString());
    }


//    @PostMapping(value = {"/users", "/users/"}, produces = "application/json")
//    @PreAuthorize("hasAuthority('OP_ADD_USER')")
//    public Users addUser(@RequestBody Users users) {
//        return userService.registerUser(users);
//    }


    @PostMapping(value = {"/users/{id}/addresses", "/users/{id}/addresses/"})
    @PreAuthorize("hasAuthority('OP_ACCESS_PUBLIC')")
    public ResponseEntity<Object> addAddressForUser(@PathVariable(value = "id") Long id, @ModelAttribute Addresses addresses, HttpServletRequest request) {
        try {
            JwtUtils jwtUtils = new JwtUtils();
            Long user_id = jwtUtils.getUserId(request);
            //check token is valid
            if (user_id == id) {
                Optional<Users> user = this.userService.findUser(user_id);
                if (user.isPresent()) {
                    addresses.setUsers(user.get());
                    Addresses savedAddress = this.addressesService.saveAddress(addresses);
                    return ResponseEntity.ok()
                            .body(savedAddress);
                } else {
                    return new ResponseEntity<>(
                            JsonResponseBodyTemplate.
                                    createResponseJson("fail", HttpStatus.FORBIDDEN.value(), "توکن معتبر نیست").toString(),
                            HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(
                        JsonResponseBodyTemplate.
                                createResponseJson("fail", HttpStatus.FORBIDDEN.value(), "توکن   و آی دی معتبر نیست").toString(),
                        HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = {"/users/addresses/{id}", "/users/addresses/{id}/"})
    @PreAuthorize("hasAuthority('OP_ACCESS_PUBLIC')")
    public ResponseEntity<Object> updateAddressOfUser(@PathVariable(value = "id") Long id, @ModelAttribute Addresses addresses, HttpServletRequest request) {
        try {
            JwtUtils jwtUtils = new JwtUtils();
            Long user_id = jwtUtils.getUserId(request);
            //check token is valid
            Optional<Users> user = this.userService.findUser(user_id);
            Optional<Addresses> address = this.addressesService.findById(id);
            if (address.isPresent() && user.isPresent()) {
                addresses.setUsers(user.get());
                Addresses updatedAddress = this.addressesService.saveAddress(addresses);
                return ResponseEntity.ok()
                        .body(updatedAddress);
            } else {
                return new ResponseEntity<>(
                        JsonResponseBodyTemplate.
                                createResponseJson("fail", HttpStatus.FORBIDDEN.value(), "توکن معتبر نیست").toString(),
                        HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = {"/users", "/users/"})
    @PreAuthorize("hasAuthority('OP_ADD_USER')")
    public ResponseEntity<Object> addUser(@ModelAttribute Users users) {
        try {
            if (userService.findUserByMobile(users.getMobile()) == null) {
                Users user_saved = userService.registerUser(users);
                return ResponseEntity.ok()
                        .body(user_saved);
            } else {
                return new ResponseEntity<>(
                        JsonResponseBodyTemplate.
                                createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), "کاربر با این شماره قبلا ثبت شده است").toString(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping(value = {"/users/{id}", "/users/{id}"})
    @PreAuthorize("hasAuthority('OP_ADD_USER')")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id,
                                             @RequestParam String name,
                                             @RequestParam String family,
                                             @RequestParam String email,
                                             @RequestParam String address,
                                             @RequestParam(required = false) MultipartFile file
    ) {


        try {
            Users user_saved = userService.updateUser(id, name, family, email, address, file);
            if (user_saved != null) {
                return ResponseEntity.ok()
                        .body(user_saved);
            } else {
                return new ResponseEntity<>(
                        JsonResponseBodyTemplate.
                                createResponseJson("fail", HttpStatus.NOT_FOUND.value(), "کاربر یافت نشد").toString(),
                        HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping(value = {"/users/addresses/{id}", "/users/addresses/{id}/"})
    @PreAuthorize("hasAuthority('OP_ACCESS_PUBLIC')")
    public ResponseEntity<Object> deleteAddresses(@PathVariable(value = "id") Long id) {
        try {
            this.addressesService.deleteAddresses(id);
            return ResponseEntity.ok()
                    .body(JsonResponseBodyTemplate
                            .createResponseJson("success", HttpStatus.OK.value(), "آدرس با موفقیت حذف شد").toString());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = {"/users/{id}", "/users/{id}/"})
    @PreAuthorize("hasAuthority('OP_ACCESS_DELETE')")
    public ResponseEntity<Object> deleteUsers(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok()
                    .body(JsonResponseBodyTemplate
                            .createResponseJson("success", response.getStatus(), "کاربر با موفقیت حذف شد").toString());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    JsonResponseBodyTemplate.
                            createResponseJson("fail", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()).toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
