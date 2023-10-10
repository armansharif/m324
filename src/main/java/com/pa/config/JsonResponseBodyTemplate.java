package com.pa.config;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonResponseBodyTemplate {

    /*
    200 (OK)
    201 (Created)
    400 (Bad Request)
    401 (Unauthorized)
    403 (Forbidden)
    404 (Not Found)
    405 (Method Not Allowed)
      */
 private String status;
 private int code;
 private String message;
 private String data;

 public JsonResponseBodyTemplate() {

    }

    public static JSONObject createResponseJson(String status, int code, String message){
        JSONObject myResponse =  new JSONObject() .put("status",status).put("code",code).put("message",message) ;
        return myResponse;
    }
    public JSONObject createResponseJsonForGetObject(String status, int code, String message, String data){
        return new JSONObject().put("status",status).put("code",code).put("message",message).put("data",data);
    }


}
