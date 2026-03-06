package com.dtd.vehiclestackcommunication.exception;

/**
 * The class Invalid parameter exception.
 */
public class InvalidParameterException extends RuntimeException {

    private static final long serialVersionUID = 1768652544129396165L;

    /**
     * Instantiates a new Invalid parameter exception.
     *
     * @param message the message
     */
    public InvalidParameterException(String message) {
        super(message);
    }
}