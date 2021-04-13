package com.mmt.flights.planner.service.route.data;

import com.mmt.flights.planner.service.route.planning.graph.Graph;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * Data loader api
 * Data loader has the responsibility to load data (data represents a graph here)
 *
 * @author ritesh
 * @version 1.0.0
 */
public interface DataLoader extends InitializingBean {

    /**
     * Load the data and return the graph representation of the data
     *
     * @return the data graph
     * @throws IOException if any error occurs while loading the data
     */
    Graph load() throws IOException;
}
