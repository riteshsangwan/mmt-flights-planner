package com.mmt.flights.planner.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Error details for api client
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
@Builder
public class ApiClientErrorDetail {

    /**
     * the field name that has gone wrong.
     */
    private String field;

    /**
     * The message why field has gone wrong.
     */
    private String message;
}
