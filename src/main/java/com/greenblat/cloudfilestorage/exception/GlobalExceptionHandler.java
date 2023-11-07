package com.greenblat.cloudfilestorage.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public String handleResourceAlreadyExistsException(ResourceAlreadyExistsException e,
                                                       Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/409";
    }
}
