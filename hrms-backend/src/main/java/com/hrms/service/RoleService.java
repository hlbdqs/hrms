package com.hrms.service;

import com.hrms.entity.SysRole;

import java.util.List;

/**
 * 角色业务层接口。
 */
public interface RoleService {

    List<SysRole> listAll();

    List<Long> getPermissionIds(Long roleId);

    SysRole create(SysRole role);

    SysRole update(Long id, SysRole role);

    void delete(Long id);

    void assignPermissions(Long roleId, List<Long> permissionIds);
}
