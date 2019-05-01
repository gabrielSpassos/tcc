package com.college.transfer.controllers.error;

import com.college.transfer.controllers.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalErrorHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleHttpException(Throwable throwable) {
        logger.error("Erro não esperado");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO("Unexpected Error"));
    }



    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(ServletRequestBindingException e) {
        return new ErrorDTO("Missing field");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(e.getMessage());
        return new ErrorDTO(errorMessage);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ErrorDTO("Invalid parameter");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ErrorDTO("Missing field");
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorDTO("Invalid input data");
    }
}
