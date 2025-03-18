package com.team.updevic001.model.dtos.exception;

public record ErrorResponse(
            int status,
        String message,
        String details,
        String errorTime) {

}
