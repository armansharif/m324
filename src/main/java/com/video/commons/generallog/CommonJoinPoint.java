package com.video.commons.generallog;

import org.aspectj.lang.annotation.Pointcut;

public class CommonJoinPoint {

    @Pointcut("execution(* com.video.modules.video.controller.VideoController.*(..))")
    public void controllerExecution(){
        System.out.println("====================controllerExecution");
    };

}