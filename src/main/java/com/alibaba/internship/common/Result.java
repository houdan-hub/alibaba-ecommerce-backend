package com.alibaba.internship.common;

/**
 * Standardised API response wrapper.
 * Every controller returns {@code Result<T>} so the frontend
 * always sees the same JSON shape: { code, message, data }.
 */
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result() {}

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ---- factory helpers ----

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(),
                            ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(ResultCode rc) {
        return new Result<>(rc.getCode(), rc.getMessage(), null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // ---- getters / setters ----

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
