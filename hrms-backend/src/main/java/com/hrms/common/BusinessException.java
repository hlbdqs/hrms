package com.hrms.common;

/**
 * 业务异常，用于在 Service 层抛出并交由全局异常处理器统一返回。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
