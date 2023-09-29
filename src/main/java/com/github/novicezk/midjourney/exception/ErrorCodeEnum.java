package com.github.novicezk.midjourney.exception;

import lombok.Getter;
import lombok.ToString;

import lombok.Getter;

public enum ErrorCodeEnum {

    PARAM_CHECK_ERROR(1400, "参数校验失败"),
    PERMISSION_CHECK_ERROR(1401, "no permission"),
    DUPLICATE_CHECK_ERROR(1402, "the resource is duplicate"),
    NOT_FOUND(1404, "资源不存在"),
    REQUEST_FORMAT_ERROR(1406, "Request format error"),
    MISSING_REQUEST_PARAMETERS(1480, "Missing request parameters"),
    METHOD_NOT_ALLOWED(1405, "请求的方法不被容许"),
    INTERNAL_SERVER_ERROR(1500, "服务器内部处理错误"),
    BUSINESS_ERROR(1400,"业务逻辑异常"),
    BUSINESS_CHECK_ERROR(1410,"Check for errors of business param"),
    TOO_MANY_CATEGORY(1413, "Request Entity Too Large"),
    DIRTY_DATA(1414, "系统存在脏数据"),
    RPC_ERROR(1415, "调用外部系统失败"),
    OSS_ERROR(1500, "oss错误"),
    DOCKING_STORE_BASE_INFO_ERROR(3007, "商品中心同步大运营门店基础信息数据失败, 请检查: 门店编码/门店名称-"),
    DOCKING_DELIVERY_STORE_BASE_INFO_ERROR(3008, "商品中心同步订货是否配送信息数据失败, 请检查: 门店编码/门店名称-"),
    UPDATE_BOM_ERROR(1501, "操作bom失败");
    ;

    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 编码
     */
    @Getter
    private int code;

    /**
     * 描述
     */
    @Getter
    private String desc;
}