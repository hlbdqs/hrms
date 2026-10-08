package com.hrms.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.common.Log;
import com.hrms.entity.SysLog;
import com.hrms.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 审计日志切面：拦截标注了 @Log 的方法，记录操作人、方法、参数（脱敏）、耗时、IP。
 */
@Aspect
@Component
public class LogAspect {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint pjp, Log log) throws Throwable {
        long start = System.currentTimeMillis();
        SysLog sysLog = new SysLog();
        sysLog.setOperation(log.value());
        sysLog.setMethod(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        sysLog.setUsername(currentUsername());
        sysLog.setIp(clientIp());
        sysLog.setParams(sanitize(pjp.getArgs()));
        try {
            Object result = pjp.proceed();
            sysLog.setStatus(1);
            return result;
        } catch (Throwable t) {
            sysLog.setStatus(0);
            throw t;
        } finally {
            sysLog.setCostTime(System.currentTimeMillis() - start);
            try {
                logService.save(sysLog);
            } catch (Exception ignored) {
                // 日志记录失败不影响业务
            }
        }
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    private String clientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "";
        }
        HttpServletRequest request = attrs.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip != null && ip.length() > 64 ? ip.substring(0, 64) : ip;
    }

    private String sanitize(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        try {
            String json = OBJECT_MAPPER.writeValueAsString(args);
            // 脱敏密码类字段
            json = json.replaceAll("(\"(?:oldPassword|newPassword|password)\"\\s*:\\s*\")[^\"]*(\")", "$1***$2");
            return json.length() > 1000 ? json.substring(0, 1000) : json;
        } catch (Exception e) {
            return "";
        }
    }
}
