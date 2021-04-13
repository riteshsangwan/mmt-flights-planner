package com.mmt.flights.planner.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Custom application listener to listen for {@link ApplicationReadyEvent}
 * {@link ApplicationReadyEvent} is only fired when spring application is initialized and all beans are initialized
 * including their pre/post hooks are fired
 *
 * @author ritesh
 * @version 1.0.0
 */
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * private static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    /**
     * Called when {@link ApplicationReadyEvent} event is fired
     * We don't do anything here, just simply log but custom functionality can be added here
     * make sure not to add any long running jobs here because this function is called in main method
     *
     * @param event the application event that was fired
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("received application ready event, application is ready to serve requests");
    }
}