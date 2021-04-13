package com.mmt.flights.planner.service.route.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Route search request
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
public class SearchRequest {

    /**
     * The start point
     */
    private String start;

    /**
     * The destination
     */
    private String destination;

    /**
     * Return the top K results
     */
    private Integer k;
}
