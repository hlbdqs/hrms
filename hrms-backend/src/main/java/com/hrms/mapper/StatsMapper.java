package com.hrms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 统计数据访问层。
 */
@Mapper
public interface StatsMapper {

    @Select("SELECT COUNT(*) FROM employee")
    long countEmployees();

    @Select("SELECT COUNT(*) FROM department")
    long countDepartments();

    @Select("SELECT COUNT(*) FROM sys_role")
    long countRoles();

    @Select("SELECT COUNT(*) FROM employee WHERE status = 1")
    long countActiveEmployees();

    @Select("SELECT d.name AS name, COUNT(e.id) AS cnt " +
            "FROM department d LEFT JOIN employee e ON e.department_id = d.id " +
            "GROUP BY d.id, d.name ORDER BY d.id")
    List<Map<String, Object>> countByDepartment();

    @Select("SELECT gender AS gender, COUNT(*) AS cnt FROM employee GROUP BY gender")
    List<Map<String, Object>> countByGender();

    @Select("SELECT status AS status, COUNT(*) AS cnt FROM employee GROUP BY status")
    List<Map<String, Object>> countByStatus();
}
