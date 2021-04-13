package com.mmt.flights.planner.service.route.planning.cache;

import com.mmt.flights.planner.service.route.planning.graph.Airport;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent all the airports in memory
 * this serves cache purpose, initially the data is loaded from file system or some external storage and than is cached
 *
 * @author ritesh
 * @version 1.0.0
 */
public final class Airports {

    /**
     * represents the airports in memory cache
     */
    private static Map<String, Airport> cache = new HashMap<>();

    /**
     * we don't want anyone to initialize this class
     */
    private Airports() {
    }

    /**
     * Get an airport object from cache using the code
     *
     * @param code the code
     * @return the airport instance or null if doesn't exist
     */
    public static Airport fromCode(String code) {
        return cache.get(code);
    }

    /**
     * Add an airport to cache
     *
     * @param airport the airport instance
     */
    public static void add(Airport airport) {
        cache.put(airport.getCode(), airport);
    }
}
