package com.mmt.flights.planner;

/**
 * Api's error codes returned by the server
 * For all error codes we are adding E1 as prefix
 * 1 indicates that these are version E1 error codes. Any evolution may use E2 as prefix
 *
 * @author ritesh
 * @version 1.0.0
 */
public final class ErrorCodes {

    /**
     * Indicate an error in which authorization header is missing
     */
    public static final String AUTHORIZATION_HEADER_MISSING = "E1-1000";


    /////////////////////////////////////// AUTHORIZATION & AUTHENTICATION ERRORS //////////////////////////////////////
    /////////////////////////////////////////// ERROR CODES BETWEEN 1000-2000 //////////////////////////////////////////

    /**
     * Indicate an error in which client api key header is not specified
     */
    public static final String CLIENT_API_KEY_HEADER_MISSING = "E1-1100";

    /**
     * Indicate an error in which client calling the api don't have access to consume the api's
     */
    public static final String CLIENT_NOT_WHITELISTED = "E1-1110";

    /////////////////////////////////////// INTERNAL SERVER ERRORS /////////////////////////////////////////////////////
    //////////////////////////////////// ERROR CODES BETWEEN 2500-3500 /////////////////////////////////////////////////

    /**
     * This error code indicates the error condition server is not able to identify and prevent beforehand
     */
    public static final String SERVER_ERROR = "E1-2500";

    /**
     * Generic 400 error code
     */
    public static final String GENERIC_400_ERROR = "E1-3000";

    /**
     * Indicate an error code which states that some mandatory parameter in api is not specified by client
     */
    public static final String MANDATORY_PARAMETER_MISSING = "E1-7000";

    /**
     * Indicate an error code which states that some mandatory parameter in api has invalid value
     */
    public static final String MANDATORY_PARAMETER_INVALID = "E1-7010";

    /**
     * We don't want any to instantiate this
     */
    private ErrorCodes() {
    }
}
