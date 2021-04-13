package com.mmt.flights.planner.service.route.model;

import com.mmt.flights.planner.service.route.planning.graph.Flight;
import com.mmt.flights.planner.service.route.planning.graph.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The actual plan for a search request
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
@Builder
public class Plan {

    /**
     * Direct fights
     */
    private List<Route> directFlights;

    /**
     * Other cheapest flights
     */
    private List<Route> cheapestFlights;
}
