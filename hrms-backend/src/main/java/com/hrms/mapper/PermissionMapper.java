package com.hrms.mapper;

import com.hrms.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限数据访问层。
 */
@Mapper
public interface PermissionMapper {

    @Select("SELECT * FROM sys_permission ORDER BY id")
    List<SysPermission> selectAll();
}
