package com.hrms.service;

import com.hrms.common.PageResult;
import com.hrms.entity.SysLog;

/**
 * 审计日志业务层接口。
 */
public interface LogService {

    void save(SysLog log);

    PageResult<SysLog> page(int page, int size, String keyword);
}
