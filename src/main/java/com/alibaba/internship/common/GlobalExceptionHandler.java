package com.alibaba.internship.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler.
 *
 * <p>Catches exceptions thrown from any controller and converts
 * them into the standard {@link Result} JSON response, so the
 * frontend never sees raw stack traces.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Business exceptions we throw ourselves. */
    @ExceptionHandler(CommonException.class)
    public Result<Void> handleCommon(CommonException e) {
        log.warn("business exception: code={}, msg={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /** @Valid / @Validated validation failures. */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidation(Exception e) {
        log.warn("validation failed: {}", e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR);
    }

    /** Catch-all for unexpected errors. */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleAll(Exception e) {
        log.error("unexpected error", e);
        return Result.error(ResultCode.INTERNAL_ERROR);
    }
}
