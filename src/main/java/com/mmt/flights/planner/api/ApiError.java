package com.mmt.flights.planner.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * General api error schema thrown by service for 401, 403, and 5xx status codes
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
@SuperBuilder
public class ApiError {

    /**
     * The service name which has thrown the error.
     */
    private String serviceName;

    /**
     * The service version which has thrrow the error.
     */
    private String serviceVersion;

    /**
     * The error code. Client's must have error codes vocabulary to translate this error code to proper message.
     */
    private String code;

    /**
     * The server defined error message for this error
     */
    private String message;

    /**
     * Any additional terchnical error details that can will be used for debugging. These error details will be submitted for error analytics.
     */
    private Map<String, Object> info;
}