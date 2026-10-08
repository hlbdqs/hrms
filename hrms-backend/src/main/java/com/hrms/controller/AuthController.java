package com.hrms.controller;

import com.hrms.common.Log;
import com.hrms.common.Result;
import com.hrms.dto.ChangePasswordRequest;
import com.hrms.dto.LoginRequest;
import com.hrms.dto.LoginResponse;
import com.hrms.entity.Employee;
import com.hrms.mapper.EmployeeMapper;
import com.hrms.security.JwtUtil;
import com.hrms.security.LoginAttemptService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 认证接口。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          EmployeeMapper employeeMapper, PasswordEncoder passwordEncoder,
                          LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.employeeMapper = employeeMapper;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    /** 登录：校验用户名密码，签发 JWT */
    @Log("登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        // 输入校验（AI 安全：所有用户可控输入均视为不可信，先校验再使用）
        if (request.getUsername() == null || request.getUsername().isBlank() || request.getUsername().length() > 64) {
            return Result.fail(400, "用户名不能为空且长度不超过 64");
        }
        if (request.getPassword() == null || request.getPassword().isBlank() || request.getPassword().length() > 100) {
            return Result.fail(400, "密码不能为空且长度不超过 100");
        }

        String username = request.getUsername().trim();
        if (loginAttemptService.isBlocked(username)) {
            return Result.fail(429, "登录失败次数过多，请 15 分钟后再试");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            UserDetails user = (UserDetails) authentication.getPrincipal();
            List<String> authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            String token = jwtUtil.generateToken(user.getUsername(), authorities);
            loginAttemptService.clear(username);
            return Result.ok(new LoginResponse(token, user.getUsername(), authorities));
        } catch (DisabledException e) {
            return Result.fail(403, "账号已禁用");
        } catch (AuthenticationException e) {
            loginAttemptService.recordFailure(username);
            return Result.fail(401, "用户名或密码错误");
        }
    }

    /** 修改当前登录用户密码（需已登录，校验原密码后 BCrypt 加密入库） */
    @Log("修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        if (request.getOldPassword() == null || request.getOldPassword().isBlank()) {
            return Result.fail(400, "原密码不能为空");
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6 || request.getNewPassword().length() > 100) {
            return Result.fail(400, "新密码长度须为 6-100");
        }

        String username = authentication.getName();
        Employee employee = employeeMapper.selectByUsername(username);
        if (employee == null) {
            return Result.fail(404, "用户不存在");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), employee.getPassword())) {
            return Result.fail(400, "原密码错误");
        }
        employeeMapper.updatePassword(employee.getId(), passwordEncoder.encode(request.getNewPassword()));
        return Result.ok();
    }
}
