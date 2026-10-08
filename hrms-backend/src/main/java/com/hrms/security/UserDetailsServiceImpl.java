package com.hrms.security;

import com.hrms.entity.Employee;
import com.hrms.mapper.AuthMapper;
import com.hrms.mapper.EmployeeMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库加载用户及其角色、权限（RBAC）。
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeMapper employeeMapper;
    private final AuthMapper authMapper;

    public UserDetailsServiceImpl(EmployeeMapper employeeMapper, AuthMapper authMapper) {
        this.employeeMapper = employeeMapper;
        this.authMapper = authMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeMapper.selectByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 角色 -> ROLE_ 前缀
        for (String role : authMapper.selectRoleCodesByEmployeeId(employee.getId())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        // 权限编码，如 employee:read
        for (String perm : authMapper.selectPermissionCodesByEmployeeId(employee.getId())) {
            authorities.add(new SimpleGrantedAuthority(perm));
        }

        return new User(employee.getUsername(), employee.getPassword(), authorities);
    }
}
