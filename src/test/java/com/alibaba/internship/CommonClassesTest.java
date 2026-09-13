package com.alibaba.internship;

import com.alibaba.internship.common.CommonException;
import com.alibaba.internship.common.Result;
import com.alibaba.internship.common.ResultCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic unit tests for common classes.
 * Run with: mvn test
 */
class CommonClassesTest {

    @Test
    void resultSuccessContainsData() {
        Result<String> r = Result.success("hello");
        assertEquals(200, r.getCode());
        assertEquals("success", r.getMessage());
        assertEquals("hello", r.getData());
    }

    @Test
    void resultErrorCarriesCodeAndMessage() {
        Result<Void> r = Result.error(ResultCode.GOODS_NOT_FOUND);
        assertEquals(3001, r.getCode());
        assertEquals("goods not found", r.getMessage());
        assertNull(r.getData());
    }

    @Test
    void commonExceptionCarriesCode() {
        CommonException e = new CommonException(ResultCode.ORDER_ALREADY_PAID);
        assertEquals(4002, e.getCode());
        assertEquals("order already paid", e.getMessage());
    }

    @Test
    void commonExceptionCustomMessage() {
        CommonException e = new CommonException(9999, "custom error");
        assertEquals(9999, e.getCode());
        assertEquals("custom error", e.getMessage());
    }
}
