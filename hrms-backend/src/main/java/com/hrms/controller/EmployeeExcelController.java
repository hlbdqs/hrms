package com.hrms.controller;

import com.alibaba.excel.EasyExcel;
import com.hrms.common.Result;
import com.hrms.dto.EmployeeExcel;
import com.hrms.entity.Employee;
import com.hrms.mapper.EmployeeMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工 Excel 导入导出接口。
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeExcelController {

    private final EmployeeMapper employeeMapper;

    public EmployeeExcelController(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /** 导出员工为 xlsx */
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('employee:read')")
    public void export(HttpServletResponse response) throws IOException {
        List<Employee> employees = employeeMapper.selectAll();
        List<EmployeeExcel> rows = new ArrayList<>();
        for (Employee e : employees) {
            rows.add(toExcel(e));
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("员工列表", StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), EmployeeExcel.class).sheet("员工").doWrite(rows);
    }

    /** 从 xlsx 批量导入员工 */
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('employee:write')")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.fail(400, "请上传文件");
        }
        List<EmployeeExcel> rows = EasyExcel.read(file.getInputStream())
                .head(EmployeeExcel.class)
                .sheet()
                .doReadSync();

        int success = 0;
        int fail = 0;
        if (rows != null) {
            for (EmployeeExcel row : rows) {
                if (row.getEmployeeNo() == null || row.getEmployeeNo().isBlank()
                        || row.getName() == null || row.getName().isBlank()) {
                    fail++;
                    continue;
                }
                try {
                    Employee e = new Employee();
                    e.setEmployeeNo(row.getEmployeeNo().trim());
                    e.setName(row.getName().trim());
                    e.setGender(row.getGender());
                    e.setPhone(row.getPhone());
                    e.setEmail(row.getEmail());
                    e.setDepartmentId(row.getDepartmentId());
                    e.setStatus(row.getStatus() == null ? 1 : row.getStatus());
                    employeeMapper.insert(e);
                    success++;
                } catch (Exception ex) {
                    fail++;
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("fail", fail);
        return Result.ok(result);
    }

    private EmployeeExcel toExcel(Employee e) {
        EmployeeExcel ex = new EmployeeExcel();
        ex.setEmployeeNo(e.getEmployeeNo());
        ex.setName(e.getName());
        ex.setGender(e.getGender());
        ex.setPhone(e.getPhone());
        ex.setEmail(e.getEmail());
        ex.setDepartmentId(e.getDepartmentId());
        ex.setStatus(e.getStatus());
        return ex;
    }
}
