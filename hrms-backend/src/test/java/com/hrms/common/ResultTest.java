package com.hrms.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 纯 JUnit 单元测试（不加载 Spring 上下文、不依赖数据库），供 CI 执行。
 */
class ResultTest {

    @Test
    void okShouldReturnCode200() {
        Result<String> r = Result.ok("data");
        assertEquals(200, r.getCode());
        assertEquals("success", r.getMessage());
        assertEquals("data", r.getData());
    }

    @Test
    void failShouldReturnCode500() {
        Result<Void> r = Result.fail("出错了");
        assertEquals(500, r.getCode());
        assertEquals("出错了", r.getMessage());
        assertNull(r.getData());
    }

    @Test
    void pageResultShouldHoldTotalAndList() {
        List<String> list = Arrays.asList("a", "b");
        PageResult<String> p = new PageResult<>(2, list);
        assertEquals(2, p.getTotal());
        assertEquals(2, p.getList().size());
    }
}
