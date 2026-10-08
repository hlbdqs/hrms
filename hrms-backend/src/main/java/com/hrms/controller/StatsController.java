package com.hrms.controller;

import com.hrms.common.Result;
import com.hrms.mapper.StatsMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计接口（登录即可访问）。
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsMapper statsMapper;

    public StatsController(StatsMapper statsMapper) {
        this.statsMapper = statsMapper;
    }

    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('employee:read')")
    public Result<Map<String, Object>> overview() {
        Map<String, Object> map = new HashMap<>();
        map.put("employees", statsMapper.countEmployees());
        map.put("departments", statsMapper.countDepartments());
        map.put("roles", statsMapper.countRoles());
        map.put("active", statsMapper.countActiveEmployees());
        return Result.ok(map);
    }

    @GetMapping("/by-department")
    @PreAuthorize("hasAuthority('employee:read')")
    public Result<List<Map<String, Object>>> byDepartment() {
        return Result.ok(statsMapper.countByDepartment());
    }

    @GetMapping("/by-gender")
    @PreAuthorize("hasAuthority('employee:read')")
    public Result<List<Map<String, Object>>> byGender() {
        return Result.ok(statsMapper.countByGender());
    }

    @GetMapping("/by-status")
    @PreAuthorize("hasAuthority('employee:read')")
    public Result<List<Map<String, Object>>> byStatus() {
        return Result.ok(statsMapper.countByStatus());
    }
}
