package com.mmt.flights.planner.service.route;

import com.mmt.flights.planner.service.exception.ClientErrorException;
import com.mmt.flights.planner.service.exception.ServiceException;
import com.mmt.flights.planner.service.route.model.Plan;
import com.mmt.flights.planner.service.route.model.SearchRequest;

/**
 * Route planning service
 *
 * @author ritesh
 * @version 1.0.0
 */
public interface PlannerService {

    /**
     * Plan a route using search request
     *
     * @param request the search request
     * @return the actual route plan
     * @throws ClientErrorException if request is invalid
     * @throws ServiceException     if any unexpected error occurs
     */
    Plan plan(SearchRequest request);
}
