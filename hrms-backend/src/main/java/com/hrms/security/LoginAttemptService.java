package com.hrms.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录失败限流：同一用户名连续失败达到阈值后锁定一段时间，防暴力破解（内存实现）。
 */
@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_SECONDS = 15 * 60;

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {
        Attempt a = attempts.get(username);
        if (a == null) {
            return false;
        }
        if (a.lockedUntil != null) {
            if (a.lockedUntil.isAfter(Instant.now())) {
                return true;
            }
            attempts.remove(username);
        }
        return false;
    }

    public void recordFailure(String username) {
        Attempt a = attempts.computeIfAbsent(username, k -> new Attempt());
        a.failCount++;
        if (a.failCount >= MAX_ATTEMPTS) {
            a.lockedUntil = Instant.now().plusSeconds(LOCK_SECONDS);
            a.failCount = 0;
        }
    }

    public void clear(String username) {
        attempts.remove(username);
    }

    private static class Attempt {
        int failCount = 0;
        Instant lockedUntil = null;
    }
}
