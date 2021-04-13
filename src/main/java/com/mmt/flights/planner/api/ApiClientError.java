package com.mmt.flights.planner.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Error response schema for 400 errors
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
@SuperBuilder
public class ApiClientError extends ApiError {

    /**
     * The error details
     */
    private List<ApiClientErrorDetail> errorDetails;
}