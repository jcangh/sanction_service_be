package com.jca.sanction.exception;

public class ResourceNotFound extends RuntimeException {
    private static final String MESSAGE = "Requested resource %s was not found under given key %s";

    public ResourceNotFound(String resource, String identifier) {
        super(String.format("Requested resource %s was not found under given key %s", resource, identifier));
    }
}
