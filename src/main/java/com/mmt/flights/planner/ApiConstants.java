package com.mmt.flights.planner;

/**
 * Application constants
 *
 * @author ritesh
 * @version 1.0.0
 */
public final class ApiConstants {

    /**
     * The client api key header name
     */
    public static final String CLIENT_API_KEY_HEADER_NAME = "x-client-api-key";

    /**
     * Request matcher path for all the api's
     */
    public static final String APIS_REQUEST_MATCHER_PATH = "/api/**";

    /**
     * Minimum layover time in minutes
     */
    public static final int MINIMUM_LAYOVER_TIME_MINS = 120;

    /**
     * Consider flights which have less than or equal to these many hops
     * DEL - LON - JFK - LAX - SFO (Valid route)
     * DEL - LON - FRA - JFK - LAX - SFO (invalid route)
     */
    public static final int MAXIMUM_HOPS = 3;

    /**
     * We don't want anyone to instantiate this
     */
    private ApiConstants() {
    }
}
