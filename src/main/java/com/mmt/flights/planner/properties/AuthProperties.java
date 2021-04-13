package com.mmt.flights.planner.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * The authentication related properties
 *
 * @author ritesh
 * @version 1.0.0
 */
@Validated
@Getter
@Setter
public class AuthProperties {

    /**
     * Api keys of all the clients which are whitelisted
     */
    @NotEmpty
    private String whitelistedClients;
}
