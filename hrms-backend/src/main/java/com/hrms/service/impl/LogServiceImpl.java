package com.hrms.service.impl;

import com.hrms.common.PageResult;
import com.hrms.entity.SysLog;
import com.hrms.mapper.LogMapper;
import com.hrms.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审计日志业务层实现。
 */
@Service
public class LogServiceImpl implements LogService {

    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public void save(SysLog log) {
        logMapper.insert(log);
    }

    @Override
    public PageResult<SysLog> page(int page, int size, String keyword) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        int offset = (page - 1) * size;
        long total = logMapper.count(keyword);
        List<SysLog> list = logMapper.selectPage(keyword, offset, size);
        return new PageResult<>(total, list);
    }
}
