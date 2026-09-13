package com.alibaba.internship.common;

/**
 * Enumeration of business result codes.
 * Keeping codes in one enum avoids magic numbers scattered
 * across controllers and makes API contracts easy to audit.
 */
public enum ResultCode {

    SUCCESS(200, "success"),

    // client errors 4xx
    BAD_REQUEST(400, "bad request"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "resource not found"),

    // business errors 5xx / custom
    INTERNAL_ERROR(500, "internal server error"),
    PARAM_ERROR(1001, "parameter validation failed"),
    USER_NOT_FOUND(2001, "user not found"),
    GOODS_NOT_FOUND(3001, "goods not found"),
    GOODS_OUT_OF_STOCK(3002, "goods out of stock"),
    ORDER_NOT_FOUND(4001, "order not found"),
    ORDER_ALREADY_PAID(4002, "order already paid"),
    ADDRESS_NOT_FOUND(5001, "address not found");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}
