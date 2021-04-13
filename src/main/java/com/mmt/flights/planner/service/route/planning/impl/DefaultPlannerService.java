package com.mmt.flights.planner.service.route.planning.impl;

import com.mmt.flights.planner.service.route.PlannerService;
import com.mmt.flights.planner.service.route.data.DataLoader;
import com.mmt.flights.planner.service.route.model.Plan;
import com.mmt.flights.planner.service.route.model.SearchRequest;
import com.mmt.flights.planner.service.route.planning.cache.Airports;
import com.mmt.flights.planner.service.route.planning.graph.Airport;
import com.mmt.flights.planner.service.route.planning.graph.Graph;
import com.mmt.flights.planner.service.route.planning.graph.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Default implementation of {@link PlannerService}
 *
 * @author ritesh
 * @version 1.0.0
 */
@Service
public class DefaultPlannerService implements PlannerService {

    /**
     * Graph object to access traversal api's
     */
    private final Graph graph;

    /**
     * Default constructor
     *
     * @param dataLoader the data loader service to load graph data
     */
    @Autowired
    public DefaultPlannerService(DataLoader dataLoader) throws IOException {
        this.graph = dataLoader.load();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plan plan(SearchRequest request) {
        Airport start = Airports.fromCode(request.getStart());
        Airport destination = Airports.fromCode(request.getDestination());

        List<Route> directFlights = graph.getDirectFlights(start, destination);
        List<Route> otherFlights = graph.topKCheapestFlights(start, destination, request.getK());

        return Plan.builder().directFlights(directFlights).cheapestFlights(otherFlights).build();
    }
}
