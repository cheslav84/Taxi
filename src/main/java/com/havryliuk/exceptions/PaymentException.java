package com.havryliuk.exceptions;

import java.io.Serial;

public final class PaymentException extends Exception {

    @Serial
    private static final long serialVersionUID = 859670872674646L;

    public PaymentException() {
        super();
    }

    public PaymentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PaymentException(final String message) {
        super(message);
    }

    public PaymentException(final Throwable cause) {
        super(cause);
    }

}
