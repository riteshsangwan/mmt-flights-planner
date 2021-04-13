package com.mmt.flights.planner.service.route.planning.graph;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a route from start to destination
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
@Builder
public class Route {

    /**
     * the cost associated with the route
     */
    private Integer cost;

    /**
     * The flight a user has to take to complete the journey
     */
    private List<Flight> flights;
}
