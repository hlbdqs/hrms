package com.hrms.dto;

import java.util.List;

/**
 * 登录响应：返回 JWT 令牌、用户名及权限列表。
 */
public class LoginResponse {

    private String token;
    private String username;
    private List<String> authorities;

    public LoginResponse() {
    }

    public LoginResponse(String token, String username, List<String> authorities) {
        this.token = token;
        this.username = username;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
