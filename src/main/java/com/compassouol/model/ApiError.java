/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter @Setter
public class ApiError {
    private int status_code;
    private String message;

    public ApiError(BindingResult bindingResult) {
        StringBuilder strError = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> strError.append(error.getDefaultMessage()));
        settupApi(400,strError.toString());
    }
    
    private void settupApi(int code, String message) {
        this.status_code = code;
        this.message = message;
    }
}
