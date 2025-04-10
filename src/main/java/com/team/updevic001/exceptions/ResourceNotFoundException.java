package com.team.updevic001.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Class<?> entity) {
        super(entity.getSimpleName().toUpperCase() + "_NOT_FOUND");
    }
}
