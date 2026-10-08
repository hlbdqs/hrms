package com.hrms.service;

import com.hrms.entity.Department;

import java.util.List;

/**
 * 部门业务层接口。
 */
public interface DepartmentService {

    List<Department> listAll();

    Department create(Department department);

    Department update(Long id, Department department);

    void delete(Long id);
}
