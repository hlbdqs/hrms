package com.hrms.service;

import com.hrms.entity.SysPermission;

import java.util.List;

/**
 * 权限业务层接口。
 */
public interface PermissionService {

    List<SysPermission> listAll();
}
