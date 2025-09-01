package com.moyobab.server.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.moyobab..service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("[START] {}.{}() args = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    safeArgs(joinPoint));
        } else {
            log.info("[START] {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
    }

    @After("execution(* com.moyobab..service..*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("[ END ] {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.moyobab..service..*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String where = "%s.%s()".formatted(
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        if (ex instanceof com.moyobab.server.global.exception.ApplicationException ae) {
            log.warn("[ERROR] {} => {} (code={})",
                    where, ae.getMessage(), ae.getErrorCase().getErrorCode());
        } else {
            log.error("[ERROR] {} => {}", where, ex.getMessage(), ex);
        }
    }

    private String safeArgs(JoinPoint jp) {
        Object[] args = jp.getArgs();
        if (args == null || args.length == 0) return "[]";
        return Arrays.stream(args)
                .map(this::safeValue)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private String safeValue(Object v) {
        if (v == null) return "null";
        if (v instanceof MultipartFile f) return "MultipartFile(size=" + f.getSize() + ")";
        if (v instanceof byte[] b) return "byte[](" + b.length + ")";
        if (v instanceof HttpServletRequest) return "HttpServletRequest";
        if (v instanceof HttpServletResponse) return "HttpServletResponse";
        if (v instanceof BindingResult br) return "BindingResult(errors=" + br.getErrorCount() + ")";
        return String.valueOf(v);
    }
}