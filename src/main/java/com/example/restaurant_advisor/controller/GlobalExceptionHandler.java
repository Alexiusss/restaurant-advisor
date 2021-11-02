package com.example.restaurant_advisor.controller;


import com.example.restaurant_advisor.error.AppException;
import com.example.restaurant_advisor.error.ErrorType;
import com.example.restaurant_advisor.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.example.restaurant_advisor.error.ErrorType.DATA_ERROR;
import static com.example.restaurant_advisor.error.ErrorType.VALIDATION_ERROR;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetExceptionView(req, e, false, ErrorType.WRONG_REQUEST, null);
    }

    @ExceptionHandler(AppException.class)
    public ModelAndView updateRestrictionException(HttpServletRequest req, AppException appEx) {
        return logAndGetExceptionView(req, appEx, false, appEx.getType(), appEx.getMsgCode());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        return logAndGetExceptionView(req, e, true, ErrorType.APP_ERROR, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null && rootMsg.toLowerCase().contains("user_email_unique")) {
            return logAndGetExceptionView(req, e, false, VALIDATION_ERROR,"exception.user.duplicateEmail");
        }
        return logAndGetExceptionView(req, e, true, DATA_ERROR, null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String code) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logException, errorType);

        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause, "message", code != null ? messageSourceAccessor.getMessage(code) : ValidationUtil.getMessage(rootCause),
                        "typeMessage", messageSourceAccessor.getMessage(errorType.getErrorCode()),
                        "status", errorType.getStatus()));
        mav.setStatus(errorType.getStatus());
        return mav;
    }
}