package com.pa.commons.exception;


public class UserServiceException extends RuntimeException{

    public UserServiceException(String message)
    {
        super(message);
    }

//        if (userEntity == null) {
//        throw new UserServiceException("User " + username + " not found");
//    }
}