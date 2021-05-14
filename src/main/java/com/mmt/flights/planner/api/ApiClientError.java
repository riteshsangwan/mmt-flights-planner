package com.mmt.flights.planner.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Error response schema for 400 errors
 *
 * @author ritesh
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ApiClientError extends ApiError {

    /**
     * The error details
     */
    private List<ApiClientErrorDetail> errorDetails;
}