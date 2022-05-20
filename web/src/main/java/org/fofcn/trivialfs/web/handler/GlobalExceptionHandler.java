package org.fofcn.trivialfs.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.common.R;
import org.fofcn.trivialfs.common.RWrapper;
import org.fofcn.trivialfs.web.exception.TrivialFsWebException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Global exception handler
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/05/07 15:43
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(TrivialFsWebException.class)
    public R handleException(TrivialFsWebException e) {
        return RWrapper.fail(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleException(HttpRequestMethodNotSupportedException e) {
        log.error("http method not support, msg: {}", e.getMessage());
        return RWrapper.fail(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error("Unknown exception, msg: {}", e.getMessage());
        return RWrapper.fail(e.getMessage() != null ? e.getMessage() : "Unknown error");
    }

    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public R handleException(BadSqlGrammarException e) {
        log.error("Bad sql grammar exception", e);
        return RWrapper.fail("BadSqlGrammarException");
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public R handleException(DataIntegrityViolationException e) {
        log.error("Data integrity violation exception", e);
        return RWrapper.fail("DataIntegrityViolationException");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        log.error("Method argument not valid exception", e);
        return RWrapper.fail("DataIntegrityViolationException");
    }
}
