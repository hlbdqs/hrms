package com.hrms.controller;

import com.hrms.common.Result;
import com.hrms.entity.Department;
import com.hrms.service.DepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门接口（RESTful）。
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /** 查询全部部门 */
    @PreAuthorize("hasAuthority('department:read')")
    @GetMapping
    public Result<List<Department>> listAll() {
        return Result.ok(departmentService.listAll());
    }

    /** 新增部门（仅管理员） */
    @PreAuthorize("hasAuthority('department:write')")
    @PostMapping
    public Result<Department> create(@RequestBody Department department) {
        return Result.ok(departmentService.create(department));
    }

    /** 更新部门（仅管理员） */
    @PreAuthorize("hasAuthority('department:write')")
    @PutMapping("/{id}")
    public Result<Department> update(@PathVariable Long id, @RequestBody Department department) {
        return Result.ok(departmentService.update(id, department));
    }

    /** 删除部门（仅管理员） */
    @PreAuthorize("hasAuthority('department:write')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return Result.ok();
    }
}
