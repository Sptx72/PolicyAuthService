package com.mmintegrations.auth_gateway.model.exception;

public class RolePermissionDeniedException extends RuntimeException {

    private final String message;

    public RolePermissionDeniedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
