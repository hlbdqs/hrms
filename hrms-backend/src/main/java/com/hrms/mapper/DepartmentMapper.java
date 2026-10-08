package com.hrms.mapper;

import com.hrms.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 部门数据访问层（MyBatis 注解方式）。
 */
@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department ORDER BY id")
    List<Department> selectAll();

    @Select("SELECT * FROM department WHERE id = #{id}")
    Department selectById(@Param("id") Long id);

    @Insert("INSERT INTO department(name, description) VALUES(#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Department department);

    @Update("UPDATE department SET name = #{name}, description = #{description} WHERE id = #{id}")
    int update(Department department);

    @Delete("DELETE FROM department WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
