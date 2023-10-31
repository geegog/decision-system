package com.inbank.decisionsystem.loan.application.exception;

import java.io.Serial;

public class BadRequestException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
