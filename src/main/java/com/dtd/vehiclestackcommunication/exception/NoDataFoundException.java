package com.dtd.vehiclestackcommunication.exception;

/**
 * This Class used to handle NoDataFound Exception at the runtime.
 */
public class NoDataFoundException extends RuntimeException {

    private static final long serialVersionUID = 4108970394919771532L;

    /**
     * Instantiates a new No data found exception.
     *
     * @param message the message
     */
    public NoDataFoundException(String message) {
        super(message);
    }

}
