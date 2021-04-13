package com.mmt.flights.planner.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Application properties/configuration mapped as pojo
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties("com.mmt.flights.planner")
@Validated
public class Planner {

    /**
     * The app name
     */
    @NotEmpty
    private String serviceName;

    /**
     * The app version
     */
    @NotEmpty
    private String serviceVersion;

    /**
     * Generic error message for the server
     */
    @NotEmpty
    private String genericErrorMessage;

    /**
     * The user for actuator endpoints
     */
    @NotEmpty
    private String actuatorEndpointsUser;

    /**
     * The password for actuator endpoints user
     */
    @NotEmpty
    private String actuatorEndpointsUserPassword;

    /**
     * Auth properties
     */
    @NotNull
    @Valid
    private AuthProperties auth;
}
