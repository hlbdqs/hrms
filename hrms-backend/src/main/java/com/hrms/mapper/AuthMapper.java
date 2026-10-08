package com.hrms.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 认证相关数据访问（角色 / 权限查询）。
 */
@Mapper
public interface AuthMapper {

    @Select("SELECT r.code FROM sys_role r JOIN employee_role er ON r.id = er.role_id WHERE er.employee_id = #{employeeId}")
    List<String> selectRoleCodesByEmployeeId(@Param("employeeId") Long employeeId);

    @Select("SELECT DISTINCT p.code FROM sys_permission p " +
            "JOIN role_permission rp ON p.id = rp.permission_id " +
            "JOIN employee_role er ON rp.role_id = er.role_id " +
            "WHERE er.employee_id = #{employeeId}")
    List<String> selectPermissionCodesByEmployeeId(@Param("employeeId") Long employeeId);

    @Insert("INSERT IGNORE INTO employee_role(employee_id, role_id) " +
            "SELECT #{employeeId}, id FROM sys_role WHERE code = #{roleCode}")
    int insertEmployeeRole(@Param("employeeId") Long employeeId, @Param("roleCode") String roleCode);

    @Select("SELECT role_id FROM employee_role WHERE employee_id = #{employeeId}")
    List<Long> selectRoleIdsByEmployeeId(@Param("employeeId") Long employeeId);

    @Delete("DELETE FROM employee_role WHERE employee_id = #{employeeId}")
    int deleteEmployeeRolesByEmployeeId(@Param("employeeId") Long employeeId);

    @Insert("INSERT INTO employee_role(employee_id, role_id) VALUES(#{employeeId}, #{roleId})")
    int insertEmployeeRoleId(@Param("employeeId") Long employeeId, @Param("roleId") Long roleId);
}
