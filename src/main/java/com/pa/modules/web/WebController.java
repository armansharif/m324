package com.pa.modules.web;

import com.pa.commons.CommonUtils;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
public class WebController {
    private UserService userService;

    private MessageSource messageSource;

    @Autowired
    public WebController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    JSONArray jsonArray = new JSONArray();

    @GetMapping("/tree")
    public String greeting(@RequestParam(name = "id", required = false, defaultValue = "") String id, Model model) {
        JSONObject jsonObject = new JSONObject();
        jsonArray = new JSONArray();
        if (CommonUtils.isNull(id)) {
            List<Users> users = userService.findAllUsersHasRef();

            for (Users user : users) {
                jsonObject = new JSONObject();
                jsonObject.put("id", user.getId() + "_" + (user.getName() == null ? user.getId() : user.getName()));//+ "_" + user.getMobile().substring(7)
                jsonObject.put("name", (user.getName() == null ? user.getId() : user.getName()));
                if ((user.getRefUser().getId()) != user.getId())
                    jsonObject.put("parent", user.getRefUser().getId() + "_" + (user.getRefUser().getName() == null ? user.getRefUser().getId() : user.getRefUser().getName()));//+ "_" + user.getRefUser().getMobile().substring(7)
                jsonArray.put(jsonObject);
            }
        } else {
            Users rootUser = userService.findUser(Long.parseLong(id)).orElse(null);
            if (CommonUtils.isNotNull(rootUser)) {
                jsonObject = new JSONObject();
                jsonObject.put("id", rootUser.getId() + "_" + (rootUser.getName() == null ? rootUser.getId() : rootUser.getName()));
                jsonObject.put("name", (rootUser.getName() == null ? rootUser.getId() : rootUser.getName()));
                if ((rootUser.getRefUser().getId()) != rootUser.getId())
                    jsonObject.put("parent", rootUser.getRefUser().getId() + "_" + (rootUser.getRefUser().getName() == null ? rootUser.getRefUser().getId() : rootUser.getRefUser().getName()));
                jsonArray.put(jsonObject);

                deapTreePresentedUser(Long.parseLong(id));
            }
//           Set<Users> users= userService.findPresentedUsers(Long.parseLong(id));
//
//
//            for (Users user : users) {
//                jsonObject = new JSONObject();
//                jsonObject.put("id", user.getId()+"_" +(user.getName() == null ? user.getId() : user.getName()) );//+ "_" + user.getMobile().substring(7)
//                jsonObject.put("name",  (user.getName() == null ? user.getId() : user.getName()) );
//                if ((user.getRefUser().getId()) != user.getId())
//                    jsonObject.put("parent", user.getRefUser().getId()+"_" +(user.getRefUser().getName() == null ? user.getRefUser().getId() : user.getRefUser().getName()) );//+ "_" + user.getRefUser().getMobile().substring(7)
//                jsonArray.put(jsonObject);
//
//            }
        }


        model.addAttribute("data", jsonArray.toString());

        return "tree";
    }

    public void deapTreePresentedUser(Long root) {
        Users rootUser = userService.findUser(root).orElse(null);
        if (CommonUtils.isNotNull(rootUser)) {
            Set<Users> childUsers = userService.findChildUsers(rootUser);
            JSONObject jsonObject = new JSONObject();

            for (Users user : childUsers) {
                if (rootUser.getId() != user.getId()) {
                    jsonObject = new JSONObject();
                    jsonObject.put("id", user.getId() + "_" + (user.getName() == null ? user.getId() : user.getName()));//+ "_" + user.getMobile().substring(7)
                    jsonObject.put("name", (user.getName() == null ? user.getId() : user.getName()));
                    if ((user.getRefUser().getId()) != user.getId())
                        jsonObject.put("parent", user.getRefUser().getId() + "_" + (user.getRefUser().getName() == null ? user.getRefUser().getId() : user.getRefUser().getName()));//+ "_" + user.getRefUser().getMobile().substring(7)
                    jsonArray.put(jsonObject);
                    deapTreePresentedUser(user.getId());
                }

            }
        }
    }
}
