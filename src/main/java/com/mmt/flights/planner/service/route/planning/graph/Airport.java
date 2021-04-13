package com.mmt.flights.planner.service.route.planning.graph;

import com.mmt.flights.planner.util.ValidationUtil;
import lombok.Getter;

import java.util.Objects;

/**
 * Represents an airport
 * an airport has a code and a country in which airport is located
 *
 * @author ritesh
 * @version 1.0.0
 */
@Getter
public class Airport {

    /**
     * The airport code
     */
    private final String code;

    /**
     * The country in which airport is located
     */
    private final String country;

    /**
     * Create airport with specified code and country
     *
     * @param code    the code
     * @param country the country
     */
    private Airport(String code, String country) {
        this.code = code;
        this.country = country;
    }

    /**
     * Get a new airport instance from code and country
     *
     * @param code    the code
     * @param country the country
     * @return the airport instance
     */
    public static Airport fromCode(String code, String country) {
        ValidationUtil.assertValid(code, "airport code must be valid");
        ValidationUtil.assertValid(country, "airport country must be valid");
        return new Airport(code, country);
    }


    /**
     * returns true if object is equals to this airport instance
     * airports are compared by their code and country values
     *
     * @param o the other object
     * @return true if equals othrwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Airport airport = (Airport) o;
        return this.code.equals(airport.code) &&
                this.country.equals(airport.country);
    }

    /**
     * Returns the hashcode for this instance
     *
     * @return the integer hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, country);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.code;
    }
}
