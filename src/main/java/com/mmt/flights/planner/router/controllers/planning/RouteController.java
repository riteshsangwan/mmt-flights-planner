package com.mmt.flights.planner.router.controllers.planning;

import com.mmt.flights.planner.router.controllers.AbstractController;
import com.mmt.flights.planner.service.exception.ClientErrorException;
import com.mmt.flights.planner.service.exception.ServiceException;
import com.mmt.flights.planner.service.route.PlannerService;
import com.mmt.flights.planner.service.route.model.Plan;
import com.mmt.flights.planner.service.route.model.SearchRequest;
import com.mmt.flights.planner.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mmt.flights.planner.ErrorCodes.MANDATORY_PARAMETER_INVALID;
import static com.mmt.flights.planner.ErrorCodes.MANDATORY_PARAMETER_MISSING;


/**
 * Planner controller
 * Exposes api for planning route from A to B
 *
 * @author ritesh
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/routes")
public class RouteController extends AbstractController {

    /**
     * private static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    /**
     * Spring resolved bean for planner service
     */
    @Autowired
    private PlannerService plannerService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterPropertiesSetInternal() throws Exception {
        ValidationUtil.assertNotNull(plannerService);
    }

    /**
     * Return a list of videos.
     *
     * @param request the video search request (categories, keyword, limit, next parameters)
     * @return 200 if operation is success
     * @throws ClientErrorException if 4xx error
     * @throws ServiceException     if any other error occurs
     */
    @GetMapping(value = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> plan(SearchRequest request) {
        assertValid(request.getStart(), "start", "start parameter is mandatory",
                MANDATORY_PARAMETER_MISSING);
        assertValid(request.getDestination(), "destination", "destination parameter is mandatory",
                MANDATORY_PARAMETER_MISSING);
        assertNotNull(request.getK(), "k", "k parameter is mandatory", MANDATORY_PARAMETER_MISSING);
        assertTrue(request.getK() > 0, "k", "k should be greater than 0", MANDATORY_PARAMETER_INVALID);

        logEntry(LOGGER, "RouteController#plan", request);

        final Plan response = plannerService.plan(request);
        return ResponseEntity.ok(response);
    }
}
