/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.exception;

/**
 *
 * @author 320099352
 */
public class ProductInexistentException extends RuntimeException {

    public ProductInexistentException() {
    }

    public ProductInexistentException(String string) {
        super(string);
    }
}
