package com.mmt.flights.planner.service.route.data.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmt.flights.planner.service.route.data.DataLoader;
import com.mmt.flights.planner.service.route.planning.cache.Airports;
import com.mmt.flights.planner.service.route.planning.cache.Flights;
import com.mmt.flights.planner.service.route.planning.graph.Airport;
import com.mmt.flights.planner.service.route.planning.graph.Flight;
import com.mmt.flights.planner.service.route.planning.graph.Graph;
import com.mmt.flights.planner.util.TimeUtil;
import com.mmt.flights.planner.util.ValidationUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of {@link DataLoader} which loads data from the resource file
 * As of version 1.0.0 this is the primary implementation
 *
 * @author ritesh
 * @version 1.0.0
 */
@Primary
@Component
public class ResourceFileDataLoader implements DataLoader {

    /**
     * Private static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFileDataLoader.class);

    /**
     * The schedule file path
     */
    private static final String SCHEDULE_FILE_PATH = "/data/ivtest-sched.json";

    /**
     * The countries file path
     */
    private static final String AIRPORT_COUNTRIES_FILE_PATH = "/data/airports-ivtest-countries.json";

    @Getter
    @Setter
    private static class Schedule {

        /**
         * The flight code
         */
        private String code;

        /**
         * The from airport
         */
        private String from;

        /**
         * the to airport
         */
        private String to;

        /**
         * The departure time
         */
        private String departure;

        /**
         * The arrival time
         */
        private String arrival;
    }

    /**
     * The object mapper bean to read json
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(objectMapper);
    }

    private void validateSchedule(Schedule schedule) {
        if (StringUtils.isBlank(schedule.from) || StringUtils.isBlank(schedule.to) ||
                StringUtils.isBlank(schedule.code) || schedule.departure == null || schedule.arrival == null) {
            throw new IllegalStateException("a flight code, from, to, arrival, departure must not be null/blank");
        }

        Flight flight = Flights.fromCode(schedule.code);
        if (flight != null) {
            String message = String.format("each flight must have a unique code. Code [%s] is duplicated", schedule.code);
            throw new IllegalStateException(message);
        }
        if (schedule.from.equalsIgnoreCase(schedule.to)) {
            String message = String.format("flight code [%s]. from [%s], to [%s] must be different", schedule.code,
                    schedule.from, schedule.to);
            throw new IllegalStateException(message);
        }
        Airport from = Airports.fromCode(schedule.from);
        if (from == null) {
            throw new IllegalStateException(String.format("invalid from airport code [%s]", schedule.from));
        }
        Airport to = Airports.fromCode(schedule.to);
        if (to == null) {
            throw new IllegalStateException(String.format("invalid to airport code [%s]", schedule.from));
        }
    }

    /**
     * {@inheritDoc}
     * TODO: May be cache flights same as airport in Flights class
     * TODO: Code has to be unique, currently codes are duplicated and needs to clean the data a little bit
     */
    @Override
    public Graph load() throws IOException {
        // first read all the airports
        final Graph graph = new Graph();

        try (InputStream inputStream = new ClassPathResource(AIRPORT_COUNTRIES_FILE_PATH).getInputStream()) {
            Map<String, String> airports = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {
            });

            // put everything in global cache
            for (Map.Entry<String, String> entry : airports.entrySet()) {
                Airport airport = Airport.fromCode(entry.getKey(), entry.getValue());
                Airports.add(airport);
                // add as a node to graph
                graph.addNode(airport);
            }
        }

        LOGGER.info("Successfully loaded airports data from classpath resource {}", AIRPORT_COUNTRIES_FILE_PATH);

        try (InputStream inputStream = new ClassPathResource(SCHEDULE_FILE_PATH).getInputStream()) {
            List<Schedule> schedules = objectMapper.readValue(inputStream, new TypeReference<List<Schedule>>() {
            });
            // flights are essentially edges in graph
            // NOTE: Validation 2 flights shouldn't have the same code
            for (Schedule schedule : schedules) {
                validateSchedule(schedule);

                Airport from = Airports.fromCode(schedule.from);
                Airport to = Airports.fromCode(schedule.to);

                Flight flight = Flight.fromCode(schedule.code, TimeUtil.pad24HourTimeFormat(schedule.departure),
                        TimeUtil.pad24HourTimeFormat(schedule.arrival), from, to);
                graph.addEdge(from, to, flight);
            }
        }

        LOGGER.info("Successfully loaded schedules data from classpath resource {}", SCHEDULE_FILE_PATH);
        return graph;
    }
}
