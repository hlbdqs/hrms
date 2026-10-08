package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.entity.SysRole;
import com.hrms.mapper.RoleMapper;
import com.hrms.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色业务层实现。
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public List<SysRole> listAll() {
        return roleMapper.selectAll();
    }

    @Override
    public List<Long> getPermissionIds(Long roleId) {
        return roleMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    public SysRole create(SysRole role) {
        roleMapper.insert(role);
        return role;
    }

    @Override
    public SysRole update(Long id, SysRole role) {
        if (roleMapper.selectById(id) == null) {
            throw new BusinessException(404, "角色不存在，id=" + id);
        }
        role.setId(id);
        roleMapper.update(role);
        return roleMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        if (roleMapper.selectById(id) == null) {
            throw new BusinessException(404, "角色不存在，id=" + id);
        }
        roleMapper.deleteRolePermissions(id);
        roleMapper.deleteEmployeeRoles(id);
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        if (roleMapper.selectById(roleId) == null) {
            throw new BusinessException(404, "角色不存在，id=" + roleId);
        }
        roleMapper.deleteRolePermissions(roleId);
        if (permissionIds != null) {
            for (Long permissionId : permissionIds) {
                roleMapper.insertRolePermission(roleId, permissionId);
            }
        }
    }
}
