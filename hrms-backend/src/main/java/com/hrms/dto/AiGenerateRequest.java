package com.hrms.dto;

/**
 * AI 文本生成请求。
 */
public class AiGenerateRequest {

    /** review=绩效评语, weekly=周报, jd=岗位JD */
    private String type;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
