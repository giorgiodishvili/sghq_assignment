package org.example.exception;

public class ManagerNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Manager Not Found by id: %s";

    public ManagerNotFoundException(final long id) {
        super(MESSAGE.formatted(id));
    }
}
