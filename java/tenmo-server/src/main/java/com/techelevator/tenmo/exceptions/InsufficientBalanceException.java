package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Insufficient balance")
public class InsufficientBalanceException extends Exception{

    public InsufficientBalanceException() {
        super("Insufficient balance");
    }
}
