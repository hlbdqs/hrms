package com.hrms.controller;

import com.hrms.common.Result;
import com.hrms.entity.SysPermission;
import com.hrms.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限接口（仅管理员）。
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<SysPermission>> listAll() {
        return Result.ok(permissionService.listAll());
    }
}
