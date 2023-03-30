package com.havryliuk.exceptions;

import java.io.Serial;

public final class UserAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2778289263748116644L;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(final String message) {
        super(message);
    }

    public UserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
