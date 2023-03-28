package com.example.sampleapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFoundResponse extends ResponseStatusException {
    public EntityNotFoundResponse() {
        super(HttpStatus.NOT_FOUND,"Entity Not Found!");
    }
}
