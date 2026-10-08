package com.hrms.mapper;

import com.hrms.entity.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 审计日志数据访问层。
 */
@Mapper
public interface LogMapper {

    @Insert("INSERT INTO sys_log(username, operation, method, params, ip, status, cost_time) " +
            "VALUES(#{username}, #{operation}, #{method}, #{params}, #{ip}, #{status}, #{costTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysLog log);

    @Select("<script>" +
            "SELECT * FROM sys_log " +
            "<where>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (username LIKE CONCAT('%', #{keyword}, '%') OR operation LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "</where>" +
            "ORDER BY id DESC LIMIT #{offset}, #{size}" +
            "</script>")
    List<SysLog> selectPage(@Param("keyword") String keyword,
                            @Param("offset") int offset,
                            @Param("size") int size);

    @Select("<script>" +
            "SELECT COUNT(*) FROM sys_log " +
            "<where>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (username LIKE CONCAT('%', #{keyword}, '%') OR operation LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "</where>" +
            "</script>")
    long count(@Param("keyword") String keyword);
}
