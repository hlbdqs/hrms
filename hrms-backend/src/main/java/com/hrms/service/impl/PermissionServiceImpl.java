package com.hrms.service.impl;

import com.hrms.entity.SysPermission;
import com.hrms.mapper.PermissionMapper;
import com.hrms.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限业务层实现。
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<SysPermission> listAll() {
        return permissionMapper.selectAll();
    }
}
