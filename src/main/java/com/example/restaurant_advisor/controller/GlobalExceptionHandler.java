package com.example.restaurant_advisor.controller;


import com.example.restaurant_advisor.error.ErrorType;
import com.example.restaurant_advisor.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, true, ErrorType.APP_ERROR);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause, "message", ValidationUtil.getMessage(rootCause), "status", httpStatus));
        mav.setStatus(httpStatus);
        return mav;
    }
}