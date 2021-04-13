package com.mmt.flights.planner.service.route.planning.graph;

import com.mmt.flights.planner.util.ValidationUtil;
import lombok.Getter;

/**
 * Represents a flight from airport a to airport b
 * A flight represents a edge from a to b
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
public class Flight {

    /**
     * The flight code
     */
    private final String code;

    /**
     * The departure time in HHMM time
     */
    private final String departure;

    /**
     * The arrival time in HHMM time
     */
    private final String arrival;

    /**
     * The departure airport
     */
    private final Airport departureAirport;

    /**
     * The arrival airport
     */
    private final Airport arrivalAirport;

    /**
     * Create flight instance from code, departure and arrival
     *
     * @param code      the code
     * @param departure the departure time
     * @param arrival   the arrival time
     */
    private Flight(String code, String departure, String arrival, Airport departureAirport, Airport arrivalAirport) {
        this.code = code;
        this.departure = departure;
        this.arrival = arrival;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    /**
     * Get flight instance from code, departure and arrival time
     *
     * @param code      the flight code
     * @param departure the departure time
     * @param arrival   the arrival time
     * @return the flight instance
     */
    public static Flight fromCode(String code, String departure, String arrival, Airport departureAirport, Airport arrivalAirport) {
        ValidationUtil.assertValid(code, "flight code must be valid");
        ValidationUtil.assertValid(departure, "departure time must be valid");
        ValidationUtil.assertValid(arrival, "arrival time must be valid");
        ValidationUtil.assertTrue(arrival.length() == 4, "arrival time should be in HHMM format");
        ValidationUtil.assertTrue(departure.length() == 4, "departure time should be in HHMM format");
        ValidationUtil.assertNotNull(departureAirport, "departure airport must not be null");
        ValidationUtil.assertNotNull(arrivalAirport, "arrival airport must not be null");

        return new Flight(code, departure, arrival, departureAirport, arrivalAirport);
    }
}
