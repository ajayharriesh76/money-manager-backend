package com.moneymanager.exception;

public class TransactionNotEditableException extends RuntimeException {
    public TransactionNotEditableException(String message) {
        super(message);
    }
}
