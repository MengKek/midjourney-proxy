package com.github.novicezk.midjourney.exception;

import java.text.MessageFormat;

/**
 * 订单异常类
 *
 * @author liuyicen
 * @date 2020-08-20 14:38
 */
public class AierException extends RuntimeException {

    private int code;
    private String message;

    public AierException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public AierException(ErrorCodeEnum errorCodeEnum) {
        super();
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMsg();
    }

    public AierException(ErrorCodeEnum errorCodeEnum, String message) {
        super();
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMsg() + message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0},{1}", this.code, this.message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
