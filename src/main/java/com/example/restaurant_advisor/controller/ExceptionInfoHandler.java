package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.error.*;
import com.example.restaurant_advisor.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.example.restaurant_advisor.error.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_REVIEW = "exception.review.duplicateForUser";

    // https://stackoverflow.com/a/42422568
    private static final Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            "user_email_unique", EXCEPTION_DUPLICATE_EMAIL,
            "reviews_unique_user_restaurant_idx", EXCEPTION_DUPLICATE_REVIEW);

    private final MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorInfo> updateRestrictionError(HttpServletRequest req, AppException appEx) {
        return logAndGetErrorInfo(req, appEx, false, appEx.getType(), messageSourceAccessor.getMessage(appEx.getMsgCode()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_I18N_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, messageSourceAccessor.getMessage(entry.getValue()));
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace, errorType);
        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        messageSourceAccessor.getMessage(errorType.getErrorCode()),
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}