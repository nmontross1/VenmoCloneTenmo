package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transfer ID not found.")
public class TransferNotFoundException extends Exception {

    public TransferNotFoundException(){
        super("Transfer ID not found.");
    }

}
