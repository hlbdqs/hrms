package com.hrms.service.impl;

import com.hrms.common.BusinessException;
import com.hrms.common.PageResult;
import com.hrms.entity.Employee;
import com.hrms.mapper.EmployeeMapper;
import com.hrms.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工业务层实现。
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Override
    public PageResult<Employee> page(int page, int size, String keyword) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        int offset = (page - 1) * size;
        long total = employeeMapper.count(keyword);
        List<Employee> list = employeeMapper.selectPage(keyword, offset, size);
        return new PageResult<>(total, list);
    }

    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new BusinessException(404, "员工不存在，id=" + id);
        }
        return employee;
    }

    @Override
    public Employee create(Employee employee) {
        if (employee.getStatus() == null) {
            employee.setStatus(1);
        }
        employeeMapper.insert(employee);
        return employee;
    }

    @Override
    public Employee update(Long id, Employee employee) {
        getById(id);
        employee.setId(id);
        employeeMapper.update(employee);
        return employeeMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        employeeMapper.deleteById(id);
    }
}
