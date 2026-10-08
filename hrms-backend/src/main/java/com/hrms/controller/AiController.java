package com.hrms.controller;

import com.hrms.ai.AiService;
import com.hrms.common.Result;
import com.hrms.dto.AiChatRequest;
import com.hrms.dto.AiGenerateRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 接口（登录即可访问）。
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private static final int MAX_LENGTH = 2000;

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /** AI 智能问答 */
    @PostMapping("/chat")
    @PreAuthorize("hasAuthority('employee:read')")
    public Result<Map<String, String>> chat(@RequestBody AiChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return Result.fail(400, "问题不能为空");
        }
        if (request.getMessage().length() > MAX_LENGTH) {
            return Result.fail(400, "问题长度不能超过 " + MAX_LENGTH);
        }
        Map<String, String> data = new HashMap<>();
        data.put("reply", aiService.answer(request.getMessage()));
        return Result.ok(data);
    }

    /** AI 文本生成 */
    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('employee:read')")
    public Result<Map<String, String>> generate(@RequestBody AiGenerateRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            return Result.fail(400, "内容不能为空");
        }
        if (request.getContent().length() > MAX_LENGTH) {
            return Result.fail(400, "内容长度不能超过 " + MAX_LENGTH);
        }
        Map<String, String> data = new HashMap<>();
        data.put("reply", aiService.generate(request.getType() == null ? "review" : request.getType(), request.getContent()));
        return Result.ok(data);
    }
}
