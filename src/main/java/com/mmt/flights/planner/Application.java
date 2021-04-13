package com.mmt.flights.planner;

import com.mmt.flights.planner.config.PlannerConfiguration;
import com.mmt.flights.planner.startup.ApplicationReadyEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * The root application class
 * This class has main method and that is the JVM entry point
 *
 * @author ritesh
 * @version 1.0.0
 */
public class Application {

    /**
     * application ready event listener
     * This listener is used to schedule cron jobs
     */
    private static final ApplicationListener<ApplicationReadyEvent> listener = new ApplicationReadyEventListener();

    /**
     * Application main entry point
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplicationBuilder()
                .sources(PlannerConfiguration.class)
                .listeners(listener)
                .build();

        application.run(args);
    }
}
