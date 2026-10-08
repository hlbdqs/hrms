package com.hrms.ai;

import com.hrms.ai.dto.ChatCompletionRequest;
import com.hrms.ai.dto.ChatCompletionResponse;
import com.hrms.ai.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * DeepSeek 大模型客户端（OpenAI 兼容接口）。
 * API Key 通过配置注入，不写死在代码中。
 */
@Component
public class DeepSeekClient {

    private final RestClient restClient;
    private final String model;

    public DeepSeekClient(RestClient.Builder builder,
                          @Value("${deepseek.base-url:https://api.deepseek.com}") String baseUrl,
                          @Value("${deepseek.api-key:}") String apiKey,
                          @Value("${deepseek.model:deepseek-chat}") String model) {
        this.model = model;
        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    /** 发送一轮对话（system + user），返回 assistant 文本 */
    public String chat(String systemPrompt, String userMessage) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(model);
        request.setMessages(List.of(
                new ChatMessage("system", systemPrompt),
                new ChatMessage("user", userMessage)
        ));
        request.setTemperature(0.7);
        request.setMaxTokens(2000);

        ChatCompletionResponse response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ChatCompletionResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "（AI 未返回内容）";
        }
        ChatMessage message = response.getChoices().get(0).getMessage();
        return message != null && message.getContent() != null ? message.getContent() : "（AI 未返回内容）";
    }
}
