package com.hrms.mapper;

import com.hrms.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 员工数据访问层（MyBatis 注解方式）。
 */
@Mapper
public interface EmployeeMapper {

    @Select("<script>" +
            "SELECT * FROM employee " +
            "<where>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (name LIKE CONCAT('%', #{keyword}, '%')" +
            "      OR employee_no LIKE CONCAT('%', #{keyword}, '%')" +
            "      OR phone LIKE CONCAT('%', #{keyword}, '%')" +
            "      OR email LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "  <if test='departmentId != null'>" +
            "    AND department_id = #{departmentId}" +
            "  </if>" +
            "</where>" +
            "ORDER BY id DESC LIMIT #{offset}, #{size}" +
            "</script>")
    List<Employee> selectPage(@Param("keyword") String keyword,
                              @Param("departmentId") Long departmentId,
                              @Param("offset") int offset,
                              @Param("size") int size);

    @Select("<script>" +
            "SELECT COUNT(*) FROM employee " +
            "<where>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (name LIKE CONCAT('%', #{keyword}, '%')" +
            "      OR employee_no LIKE CONCAT('%', #{keyword}, '%')" +
            "      OR phone LIKE CONCAT('%', #{keyword}, '%')" +
            "      OR email LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "  <if test='departmentId != null'>" +
            "    AND department_id = #{departmentId}" +
            "  </if>" +
            "</where>" +
            "</script>")
    long count(@Param("keyword") String keyword, @Param("departmentId") Long departmentId);

    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee selectById(@Param("id") Long id);

    @Insert("INSERT INTO employee(employee_no, name, username, password, gender, phone, email, department_id, status) " +
            "VALUES(#{employeeNo}, #{name}, #{username}, #{password}, #{gender}, #{phone}, #{email}, #{departmentId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Employee employee);

    @Select("SELECT * FROM employee WHERE username = #{username}")
    Employee selectByUsername(@Param("username") String username);

    @Update("UPDATE employee SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE employee SET employee_no = #{employeeNo}, name = #{name}, gender = #{gender}, " +
            "phone = #{phone}, email = #{email}, department_id = #{departmentId}, status = #{status} " +
            "WHERE id = #{id}")
    int update(Employee employee);

    @Delete("DELETE FROM employee WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
