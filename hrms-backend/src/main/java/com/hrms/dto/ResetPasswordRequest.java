package com.hrms.dto;

/**
 * 管理员重置用户密码请求参数。
 */
public class ResetPasswordRequest {

    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
