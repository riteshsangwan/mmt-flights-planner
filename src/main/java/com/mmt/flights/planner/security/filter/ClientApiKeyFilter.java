package com.mmt.flights.planner.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates that client api is specified is whitelisted client
 *
 * @author ritesh
 * @version 1.0.0
 */
public class ClientApiKeyFilter extends AbstractSecurityFilter {

    /**
     * static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApiKeyFilter.class);

    /**
     * Default constructor
     * For this filter we don't need to have authorization header validated
     */
    public ClientApiKeyFilter() {
        super(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternalInternal(String clientApiKey, String accessToken) throws Exception {
        // noop, version 1.0.0 all validations are done in parent superclass any specific validation can be added here
    }
}