package com.mmt.flights.planner.service.route.planning.cache;

import com.mmt.flights.planner.service.route.planning.graph.Flight;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent all the flights in memory
 * this serves cache purpose, initially the data is loaded from file system or some external storage and than is cached
 *
 * @author ritesh
 * @version 1.0.0
 */
public final class Flights {

    /**
     * represents the airports in memory cache
     */
    private static Map<String, Flight> cache = new HashMap<>();

    /**
     * we don't want anyone to initialize this class
     */
    private Flights() {
    }

    /**
     * Get an Flight object from cache using the code
     *
     * @param code the code
     * @return the airport instance or null if doesn't exist
     */
    public static Flight fromCode(String code) {
        return cache.get(code);
    }

    /**
     * Add an flight to cache
     *
     * @param flight the flight instance
     */
    public static void add(Flight flight) {
        cache.put(flight.getCode(), flight);
    }
}
