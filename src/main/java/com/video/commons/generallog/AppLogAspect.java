package com.video.commons.generallog;


import com.video.commons.utils.Utils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AppLogAspect {
    private final Logger LOG = LoggerFactory.getLogger(AppLogAspect.class);

    private final HttpServletRequest request;

    public AppLogAspect(HttpServletRequest request) {
        this.request = request;
    }
    @Before(value = "execution(* com.video.modules.video.service.VideoService.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("Before method:" + joinPoint.getSignature());

        System.out.println("Creating Employee with name - " );
    }

    @After(value = "execution(* com.video.modules.video.service.VideoService.*(..))")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("After method:" + joinPoint.getSignature());

        System.out.println("Successfully created Employee with name - " );
    }
    @Before(value = "execution(* com.video.modules.video.service.VideoService.*(..))")
    public void beforeAdvice2(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("____________________________Before 2 method:" + joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        String queryString = "";
        if(Utils.isNotNull(request.getQueryString())){
            queryString = request.getQueryString();
        }
        String reduce = Arrays.stream(args).reduce("", (s, s2) -> s + " " + s2).toString();
        LOG.debug("before method: {} | argument: {} | queryString: {}",
                methodName, reduce, queryString);
        System.out.println("methodName  : "+methodName + "  reduce : "+reduce + " queryString:" +queryString);
    }
}
