package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.entity.Department;
import com.hrms.mapper.DepartmentMapper;
import com.hrms.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门业务层实现。
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Department> listAll() {
        return departmentMapper.selectAll();
    }

    @Override
    public Department create(Department department) {
        departmentMapper.insert(department);
        return department;
    }

    @Override
    public Department update(Long id, Department department) {
        if (departmentMapper.selectById(id) == null) {
            throw new BusinessException(404, "部门不存在，id=" + id);
        }
        department.setId(id);
        departmentMapper.update(department);
        return departmentMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        if (departmentMapper.selectById(id) == null) {
            throw new BusinessException(404, "部门不存在，id=" + id);
        }
        departmentMapper.deleteById(id);
    }
}
