package com.mmt.flights.planner.service.exception;

import com.mmt.flights.planner.api.ApiClientErrorDetail;

import java.util.List;

/**
 * Exception class that resolves to 400 status code
 *
 * @author ritesh
 * @version 1.0.0
 */
public class ClientErrorException extends RuntimeException {

    /**
     * The error code
     */
    private final String code;

    /**
     * the Complete error details
     */
    private final List<ApiClientErrorDetail> details;

    /**
     * Create instance of client error exception with specified code and message
     *
     * @param code    the error code
     * @param message the exception message
     */
    public ClientErrorException(String code, String message) {
        this(code, message, null);
    }

    /**
     * Create instance of client error exception with specified code, message and error
     *
     * @param code    the error code
     * @param message the exception message
     * @param details the complete error details
     */
    public ClientErrorException(String code, String message, List<ApiClientErrorDetail> details) {
        this(code, message, details, null);
    }

    /**
     * Create instance of client error exception with specified code, message and error
     *
     * @param code    the error code
     * @param message the exception message
     * @param details the complete error details
     * @param cause   the cause of this exception
     */
    public ClientErrorException(String code, String message, List<ApiClientErrorDetail> details, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.details = details;
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
     * Get the value of details
     *
     * @return the value of details
     */
    public List<ApiClientErrorDetail> getDetails() {
        return details;
    }
}
