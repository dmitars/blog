package com.example.springExample.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {

    static String getStringOfErrors(BindingResult bindingResult){
        var errorList = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return String.join(", ",errorList);
    }
}
