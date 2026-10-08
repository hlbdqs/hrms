package com.hrms.controller;

import com.hrms.common.Log;
import com.hrms.common.PageResult;
import com.hrms.common.Result;
import com.hrms.dto.ResetPasswordRequest;
import com.hrms.entity.Employee;
import com.hrms.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 员工接口（RESTful）。
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /** 分页 + 关键字查询 */
    @PreAuthorize("hasAuthority('employee:read')")
    @GetMapping
    public Result<PageResult<Employee>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId) {
        return Result.ok(employeeService.page(page, size, keyword, departmentId));
    }

    /** 按 ID 查询 */
    @PreAuthorize("hasAuthority('employee:read')")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        return Result.ok(employeeService.getById(id));
    }

    /** 新增 */
    @Log("新增员工")
    @PreAuthorize("hasAuthority('employee:write')")
    @PostMapping
    public Result<Employee> create(@RequestBody Employee employee) {
        return Result.ok(employeeService.create(employee));
    }

    /** 更新 */
    @Log("修改员工")
    @PreAuthorize("hasAuthority('employee:write')")
    @PutMapping("/{id}")
    public Result<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        return Result.ok(employeeService.update(id, employee));
    }

    /** 删除（仅管理员） */
    @Log("删除员工")
    @PreAuthorize("hasAuthority('employee:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return Result.ok();
    }

    /** 查询员工角色（仅管理员） */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/roles")
    public Result<List<Long>> getRoles(@PathVariable Long id) {
        return Result.ok(employeeService.getRoleIds(id));
    }

    /** 分配员工角色（仅管理员） */
    @Log("分配角色")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/roles")
    public Result<Void> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        employeeService.assignRoles(id, roleIds);
        return Result.ok();
    }

    /** 重置员工密码（仅管理员） */
    @Log("重置密码")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest request) {
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6 || request.getNewPassword().length() > 100) {
            return Result.fail(400, "新密码长度须为 6-100");
        }
        employeeService.resetPassword(id, request.getNewPassword());
        return Result.ok();
    }
}
