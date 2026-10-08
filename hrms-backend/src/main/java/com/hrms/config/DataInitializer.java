package com.hrms.config;

import com.hrms.entity.Employee;
import com.hrms.mapper.AuthMapper;
import com.hrms.mapper.EmployeeMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 启动时初始化演示账号（密码经 BCrypt 加密后入库，绝不明文存储）。
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeMapper employeeMapper;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(EmployeeMapper employeeMapper, AuthMapper authMapper, PasswordEncoder passwordEncoder) {
        this.employeeMapper = employeeMapper;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createAccountIfAbsent("admin", "admin123", "ADMIN", "赵建国", "1010", 1, 2L, "13800001010", "zhaojianguo@hrms.com");
        createAccountIfAbsent("hr", "hr123", "HR", "孙丽华", "1011", 2, 2L, "13800001011", "sunlihua@hrms.com");
        createAccountIfAbsent("user", "user123", "EMPLOYEE", "周小燕", "1012", 2, 3L, "13800001012", "zhouxiaoyan@hrms.com");
    }

    private void createAccountIfAbsent(String username, String rawPassword, String roleCode, String name,
                                       String employeeNo, Integer gender, Long departmentId, String phone, String email) {
        if (employeeMapper.selectByUsername(username) != null) {
            return;
        }
        Employee employee = new Employee();
        employee.setEmployeeNo(employeeNo);
        employee.setName(name);
        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode(rawPassword));
        employee.setGender(gender);
        employee.setDepartmentId(departmentId);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setStatus(1);
        employeeMapper.insert(employee);
        authMapper.insertEmployeeRole(employee.getId(), roleCode);
    }
}
