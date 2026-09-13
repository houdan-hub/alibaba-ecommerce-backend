package com.alibaba.internship.common;

/**
 * Custom business exception.
 *
 * <p>Thrown manually in service-layer code when a business rule
 * is violated (e.g. out of stock, order not found). Carries a
 * {@link ResultCode} so the global handler can translate it into
 * the standard {@link Result} JSON shape.</p>
 *
 * <p>This is NOT a global-exception configuration — it is a plain
 * RuntimeException subclass used with {@code throw new CommonException(...)}.</p>
 */
public class CommonException extends RuntimeException {

    private final Integer code;

    public CommonException(ResultCode rc) {
        super(rc.getMessage());
        this.code = rc.getCode();
    }

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
