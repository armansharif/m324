package com.pa.commons.generallog;

import org.aspectj.lang.annotation.Pointcut;

public class CommonJoinPoint {

    @Pointcut("execution(* com.pa.modules.dam.controller.DamController.*(..))")
    public void controllerExecution(){
        System.out.println("====controllerExecution");
    };

}