package com.mmt.flights.planner.service.route.planning.graph;

import com.mmt.flights.planner.util.TimeUtil;
import com.mmt.flights.planner.util.ValidationUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import static com.mmt.flights.planner.ApiConstants.MAXIMUM_HOPS;
import static com.mmt.flights.planner.ApiConstants.MINIMUM_LAYOVER_TIME_MINS;

/**
 * Represents a graph
 * A graph has node and edges
 * nodes are represented as airports and edges are flights between airports
 *
 * @author ritesh
 * @version 1.0.0
 */
public class Graph {

    /**
     * Private static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

    /**
     * internally represents an edge from a node
     * A edge has destination airport and a flight that connects directly from start to destination
     * This class is just a holder for destination airport and flight between a start and destination airport
     */
    @Getter
    private class Edge {

        /**
         * the destination airport
         */
        private final Airport destination;

        /**
         * the flight from start to destination
         */
        private final Flight flight;

        /**
         * Create an edge instance
         *
         * @param destination the destination
         * @param flight      the flight (connection from start node to destination)
         */
        public Edge(Airport destination, Flight flight) {
            this.destination = destination;
            this.flight = flight;
        }
    }

    /**
     * Graph represented as adjacency list
     */
    private Map<Airport, Set<Edge>> graph;

    /**
     * instantiate a new graph object
     */
    public Graph() {
        this.graph = new HashMap<>();
    }

    /**
     * add a node to the graph
     *
     * @param airport the node
     * @return this graph instance for chaining
     */
    public Graph addNode(Airport airport) {
        ValidationUtil.assertNotNull(airport, "airport must not be null");
        graph.putIfAbsent(airport, new HashSet<>());
        return this;
    }

    /**
     * Add an edge to a node
     * Edge represents a direct connection from start to destination
     * If node is not present in graph than throw error
     * NOTE: Duplication check is not handled
     * if this function is called twice with same parameters the same edge will be added twice
     *
     * @param start       an airport (node) the starting point
     * @param destination the destination airport
     * @param flight      the flight (edge from airport)
     * @throws IllegalArgumentException if node is not present or any arguments are null
     */
    public Graph addEdge(Airport start, Airport destination, Flight flight) {
        ValidationUtil.assertNotNull(start, "start must not be null");
        ValidationUtil.assertNotNull(destination, "destination must not be null");
        ValidationUtil.assertNotNull(flight, "flight must not be null");
        if (!this.graph.containsKey(start)) {
            throw new IllegalArgumentException("node not present in graph");
        }
        this.graph.get(start).add(new Edge(destination, flight));
        return this;
    }

    /**
     * Get direct flights from start to destination
     * if the start point doesn't exist or there are no direct flights return an empty list
     *
     * @param start       the start point (airport)
     * @param destination the destination point (airport)
     * @return return a list of direct flights
     */
    public List<Route> getDirectFlights(Airport start, Airport destination) {
        if (!this.graph.containsKey(start)) {
            return new ArrayList<>();
        }
        Set<Edge> edges = this.graph.get(start);
        List<Route> routes = new ArrayList<>();
        for (Edge edge : edges) {
            if (destination.equals(edge.destination)) {
                int cost = TimeUtil.differenceInMins(edge.flight.getDeparture(), edge.flight.getArrival());
                Route route = Route.builder().cost(cost).flights(Arrays.asList(edge.flight)).build();
                routes.add(route);
            }
        }

        return routes;
    }

    /**
     * get a list of top k cheapest flights from start to destination
     * The cost is defined as the time taken from start point to destination point
     * Probably the most important api exposed by graph object
     * NOTE: Constraints for the route
     * 1. If both start and destination is in the same country, route shouldn't go out of that country (domestic travel)
     *
     * @param start       the start point
     * @param destination the destination point
     * @param k           how many top results to return
     * @return a list of size at max k or less (if there are less than k possible ways from start to destination)
     */
    public List<Route> topKCheapestFlights(Airport start, Airport destination, int k) {
        String sCountry = start.getCountry();
        String dCountry = destination.getCountry();

        if (sCountry.equalsIgnoreCase(dCountry)) {
            return shortestPathBFS(start, destination, k, sCountry);
        }
        return shortestPathBFS(start, destination, k, null);
    }

    /**
     * NOTE: Don't consider direct routes
     * shortest path from start to destination
     * Time between start and end is considered as cost
     * don't return direct flights in this route
     *
     * @param start       the start airport (in graph terms node)
     * @param destination the destination airport (in graph terms node)
     * @param k           how many paths to return (the returned paths may be less than k)
     * @param country     the country (optional) if both start and destination is in same country we don't want international haults
     * @return list of list of flights (a flight is a connection from node a to node b
     */
    private List<Route> shortestPathBFS(Airport start, Airport destination, int k, String country) {
        List<Route> journeys = new ArrayList<>();

        // we need the paths sorted in ascending order
        // whenever heap size is more than k we poll from heap so we need to keep them in descending order
        PriorityQueue<Path> topK = new PriorityQueue<>(new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                int value = o1.compareTo(o2);
                if (value < 0) {
                    return 1;
                } else if (value == 0) {
                    return 0;
                }
                return -1;
            }
        });

        PriorityQueue<GraphNode> queue = new PriorityQueue<>();
        queue.add(new GraphNode(0, start, new Path().addAirport(start)));

        while (!queue.isEmpty() && topK.size() < k) {
            GraphNode current = queue.poll();
            // if the current path has more hops than maximum allowed
            // we don't go further in that path
            if (current.path.flights.size() > MAXIMUM_HOPS) {
                continue;
            }
            // check all edges of current
            if (current.node.equals(destination)) {
                // we reached destination
                topK.add(current.path);
                if (topK.size() > k) {
                    // ideally this will never be triggered
                    Path recent = topK.poll();
                    LOGGER.debug("Start {}, destination {}, path {} is not in top {}", start, destination, recent, k);
                }
            } else {
                List<GraphNode> neighbours = getNeighbours(current, start, destination, country);
                queue.addAll(neighbours);
            }
        }

        while (!topK.isEmpty()) {
            Path path = topK.poll();
            Route route = Route.builder().cost(path.cost).flights(path.flights).build();
            journeys.add(route);
        }
        Collections.reverse(journeys);
        return journeys;
    }

    /**
     * Returns all the current node neighbours
     * if the next node from current is not destination than there would be additional layover time added as well
     * Direct flights are not returned here
     *
     * @param current the current node
     * @return the list of neighbours (may be empty) but never null
     */
    private List<GraphNode> getNeighbours(GraphNode current, Airport start, Airport destination, String country) {
        List<GraphNode> neighbours = new ArrayList<>();

        Set<Edge> edges = graph.get(current.node);
        if (CollectionUtils.isNotEmpty(edges)) {
            for (Edge edge : edges) {
                // if we have to do same country check do here
                if (StringUtils.isNotBlank(country) && !edge.destination.getCountry().equalsIgnoreCase(country)) {
                    // we don't want an international detour
                    continue;
                }
                if (current.node.equals(start) && edge.destination.equals(destination)) {
                    continue;
                }

                // if this airport already exists in path we won't be going there again
                if (current.path.airports.contains(edge.destination)) {
                    continue;
                }

                // calculate new cost for this edge
                int newCost = 0;

                // now if the flight departure time is more than or equal to
                // lading time at current + MINIMUM_LAYOVER_TIME_MINS than the person can catch flight at same day otherwise
                // the person will have to layover at airport overnight
                if (current.path.flights.size() > 0) {
                    // get the last flight user took
                    String latestArrival = current.path.flights.get(current.path.flights.size() - 1).getArrival();
                    newCost = newCost + calculateLayoverTime(latestArrival, edge);
                    // add flight time
                    newCost = newCost + TimeUtil.differenceInMins(edge.flight.getDeparture(), edge.flight.getArrival());
                } else {
                    newCost = newCost + TimeUtil.differenceInMins(edge.flight.getDeparture(), edge.flight.getArrival());
                }
                Path cloned = current.path.clone();
                cloned.addFlight(edge.flight, newCost);
                cloned.addAirport(edge.destination);

                neighbours.add(new GraphNode(newCost + current.cost, edge.destination, cloned));
            }
        }

        return neighbours;
    }

    /**
     * Calculate the layover time in mins
     * it's possible that user landed at 1300 hours and next flight is at 1600 hours so 3 hours is added to total cost
     *
     * @param latestArrival the user latest arrival time
     * @param edge          the edge
     * @return the total layover time, this will be added to cost
     */
    private int calculateLayoverTime(String latestArrival, Edge edge) {
        LocalTime arrivalTime = LocalTime.parse(latestArrival, DateTimeFormatter.ofPattern("HHmm"));
        LocalTime nextFlightTime = LocalTime.parse(latestArrival, DateTimeFormatter.ofPattern("HHmm"))
                .plusMinutes(MINIMUM_LAYOVER_TIME_MINS);

        LocalTime flightTime = LocalTime.parse(edge.flight.getDeparture(), DateTimeFormatter.ofPattern("HHmm"));

        if (nextFlightTime.isAfter(LocalTime.MIDNIGHT) || nextFlightTime.equals(LocalTime.MIDNIGHT)) {
            // wrap around add 1 day
            return TimeUtil.differenceInMins(nextFlightTime, flightTime) + MINIMUM_LAYOVER_TIME_MINS;
        }

        if (flightTime.isAfter(nextFlightTime) || flightTime.equals(nextFlightTime)) {
            // no need for overnight layover
            return TimeUtil.differenceInMins(arrivalTime, flightTime);
        }

        if (arrivalTime.equals(flightTime)) {
            // full 1440 layover
            return 1440;
        }

        if (arrivalTime.isAfter(flightTime)) {
            return TimeUtil.differenceInMins(arrivalTime, flightTime);
        }

        return TimeUtil.differenceInMins(flightTime, arrivalTime);
    }

    /**
     * represents a graph traversal node
     */
    @Getter
    @Setter
    private class GraphNode implements Comparable<GraphNode> {

        /**
         * The cumulative cost to reach this airport
         */
        private int cost;

        /**
         * The airport node
         */
        private Airport node;

        /**
         * The path taken to reach this airport node
         */
        private Path path;

        /**
         * Create instance of graph node with specified cost, node and path
         *
         * @param cost the cost
         * @param node the node
         * @param path the path
         */
        public GraphNode(int cost, Airport node, Path path) {
            this.cost = cost;
            this.node = node;
            this.path = path;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(GraphNode o) {
            if (this.cost == o.cost) {
                return this.path.compareTo(o.path);
            }
            return this.cost - o.cost;
        }

        @Override
        public String toString() {
            return "GraphNode{" +
                    "cost=" + cost +
                    ", node=" + node +
                    ", path=" + path +
                    '}';
        }
    }

    // class to represent a path from start to destination
    @Getter
    @Setter
    private class Path implements Comparable<Path> {

        /**
         * the cost associated with path
         */
        private int cost;

        /**
         * List of flights from start to destination
         * (in order)
         */
        private List<Flight> flights = new ArrayList<>();

        /**
         * all airports a user will pass through in journey
         * Both start and destination will be part of this list
         * start should be first and destination should be last in that order
         */
        private List<Airport> airports = new ArrayList<>();

        /**
         * Add a flight to the path with specified cost
         *
         * @param flight the flight object to add
         * @param cost   the cost to increment
         * @return this path for chaining
         */
        public Path addFlight(Flight flight, int cost) {
            this.flights.add(flight);
            this.cost = this.cost + cost;
            return this;
        }

        /**
         * Add an airport
         *
         * @param airport the airport object
         * @return path object for chaining
         */
        public Path addAirport(Airport airport) {
            this.airports.add(airport);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Path clone() {
            Path path = new Path();
            path.setCost(this.cost);
            path.setAirports(new ArrayList<>(this.airports));
            path.setFlights(new ArrayList<>(this.flights));
            return path;
        }

        /**
         * All the paths are compared using cost and if cost is same they are compared using no of stops
         *
         * @param p the other path object (compare with this)
         * @return -1 if this path is less than p, 0 if both are equal or +1 if this path is greater than p
         */
        @Override
        public int compareTo(Path p) {
            if (this.cost == p.cost) {
                return this.flights.size() - p.flights.size();
            }
            return this.cost - p.cost;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Path{" +
                    "cost=" + cost +
                    ", flights=" + flights +
                    '}';
        }
    }
}
