package com.revolut.moneytransfer.ex;


/**
 * The type Dao exception.
 */
public class DAOException extends RuntimeException {

    /**
     * Instantiates a new Dao exception.
     *
     * @param message the message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Dao exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}