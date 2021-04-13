package com.mmt.flights.planner.security.exception;

import org.springframework.http.HttpStatus;

/**
 * Represents an authentication exception thrown when server failed to authenticate the request
 *
 * @author ritesh
 * @version 1.0.0
 */
public class AuthenticationException extends RuntimeException {

    /**
     * The error code
     */
    private final String code;

    /**
     * the http status code which should be returned to client
     */
    private final HttpStatus status;

    /**
     * Create instance of external api exception with specified code and message
     *
     * @param code    the error code
     * @param message the exception message
     */
    public AuthenticationException(String code, String message) {
        this(code, HttpStatus.UNAUTHORIZED, message, null);
    }

    /**
     * Create instance of external api exception with specified code, http status and message
     *
     * @param code    the error code
     * @param status  the specified http status
     * @param message the exception message
     */
    public AuthenticationException(String code, HttpStatus status, String message) {
        this(code, status, message, null);
    }


    /**
     * Create instance of external api exception with specified code, message and cause
     *
     * @param code    the code for this exception
     * @param status  the http status
     * @param message the exception message
     * @param cause   the cause for this exception
     */
    public AuthenticationException(String code, HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }

    /**
     * Get the value of code
     *
     * @return the value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public HttpStatus getStatus() {
        return status;
    }
}
