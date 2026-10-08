package com.hrms.mapper;

import com.hrms.entity.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 角色数据访问层（角色管理）。
 */
@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM sys_role ORDER BY id")
    List<SysRole> selectAll();

    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(@Param("id") Long id);

    @Insert("INSERT INTO sys_role(code, name, description) VALUES(#{code}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysRole role);

    @Update("UPDATE sys_role SET code = #{code}, name = #{name}, description = #{description} WHERE id = #{id}")
    int update(SysRole role);

    @Delete("DELETE FROM sys_role WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT p.id FROM sys_permission p JOIN role_permission rp ON p.id = rp.permission_id WHERE rp.role_id = #{roleId}")
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    int deleteRolePermissions(@Param("roleId") Long roleId);

    @Delete("DELETE FROM employee_role WHERE role_id = #{roleId}")
    int deleteEmployeeRoles(@Param("roleId") Long roleId);

    @Insert("INSERT INTO role_permission(role_id, permission_id) VALUES(#{roleId}, #{permissionId})")
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
