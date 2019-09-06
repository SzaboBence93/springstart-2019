package com.nitrowise.basesrvr.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.nitrowise.basesrvr.exception.dto.ErrorJson;
import com.nitrowise.basesrvr.exception.dto.ExceptionJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity handleAccessDeniedException(AccessDeniedException ex) {
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity(new ExceptionJson("Hozzáférés megtagadva", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity handleAuthenticationException(AuthenticationException ex) {
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity(new ExceptionJson("Hozzáférés megtagadva", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (!(ex.getCause() instanceof JsonMappingException)) {
            return new ResponseEntity(new ExceptionJson(StringUtils.isBlank(ex.getMessage()) ? "Probléma merült fel a kérés kiszolgálása során" : ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            JsonMappingException e = (JsonMappingException) ex.getCause();
            ErrorJson json = new ErrorJson();
            e.getPath().forEach(reference -> json.addAttributeError(reference.getFieldName(), "Hibás formátum."));
            return new ResponseEntity(json, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @ExceptionHandler({ApiException.class})
    @ResponseBody
    public ResponseEntity handleBaseException(ApiException ex) {
        log.warn(ex.getMessage(), ex);
        return ex.hasErrors() ? new ResponseEntity(new ErrorJson(ex.getMessage(), ex.getErrors()), HttpStatus.NOT_ACCEPTABLE) : this.handleGenericRestException(ex);
    }

    private ResponseEntity handleGenericRestException(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity(new ExceptionJson(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity handleException(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity(new ExceptionJson("Hibás kérés", "", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
