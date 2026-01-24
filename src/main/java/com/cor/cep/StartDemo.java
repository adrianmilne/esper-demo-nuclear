package com.cor.cep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cor.cep.util.RandomTemperatureEventGenerator;

/**
 * Entry point for the Demo. Run this from your IDE, or from the command line using 'mvn exec:java'.
 */
public class StartDemo {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(StartDemo.class);

    
    /**
     * Main method - start the Demo!
     */
    public static void main(String[] args) throws Exception {

        LOG.debug("Starting...");

        long noOfTemperatureEvents = parseNumberOfEvents(args);

        // Load spring config
        try (ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "application-context.xml" })) {
            // Start Demo
            RandomTemperatureEventGenerator generator = appContext.getBean(RandomTemperatureEventGenerator.class);
            generator.startSendingTemperatureReadings(noOfTemperatureEvents).get();
        }
    }

    private static long parseNumberOfEvents(String[] args) {
        long noOfTemperatureEvents = 1000;
        if (args.length == 0) {
            LOG.debug("No override of number of events detected - defaulting to {} events.", noOfTemperatureEvents);
            return noOfTemperatureEvents;
        }
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: StartDemo <number-of-events>");
        }
        try {
            return Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Number of events must be numeric.", ex);
        }
    }
}
