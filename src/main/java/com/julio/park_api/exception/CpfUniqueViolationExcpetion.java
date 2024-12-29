package com.julio.park_api.exception;

public class CpfUniqueViolationExcpetion extends RuntimeException {
    public CpfUniqueViolationExcpetion(String message) {
        super(message);
    }
}
