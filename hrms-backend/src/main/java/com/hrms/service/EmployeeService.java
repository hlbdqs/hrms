package com.hrms.service;

import com.hrms.common.PageResult;
import com.hrms.entity.Employee;

import java.util.List;

/**
 * 员工业务层接口。
 */
public interface EmployeeService {

    PageResult<Employee> page(int page, int size, String keyword, Long departmentId);

    Employee getById(Long id);

    Employee create(Employee employee);

    Employee update(Long id, Employee employee);

    void delete(Long id);

    List<Long> getRoleIds(Long id);

    void assignRoles(Long id, List<Long> roleIds);

    void resetPassword(Long id, String newPassword);
}
