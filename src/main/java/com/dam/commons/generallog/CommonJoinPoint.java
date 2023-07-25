package com.dam.commons.generallog;

import org.aspectj.lang.annotation.Pointcut;

public class CommonJoinPoint {

    @Pointcut("execution(* com.dam.modules.video.controller.VideoController.*(..))")
    public void controllerExecution(){
        System.out.println("====================controllerExecution");
    };

}