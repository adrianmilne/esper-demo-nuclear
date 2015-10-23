package com.cor.cep;

import com.cor.cep.action.TemperatureCriticalAction;
import com.cor.cep.action.TemperatureMonitorAction;
import com.cor.cep.action.TemperatureWarningAction;
import com.cor.cep.event.TemperatureEvent;
import com.cor.cep.query.TemperatureQueries;
import com.espertech.esper.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

/**
 * Entry point for the Demo. Run this from your IDE, or from the command line using 'mvn exec:java'.
 */
public class EsperDemoNuclear {
    private static final Logger logger = LoggerFactory.getLogger(EsperDemoNuclear.class);

    public static void main(final String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - TEMPERATURES ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        System.out.println(sb.toString());

        long demoMaxEventCount = 1000;
        if (args.length != 1) {
            logger.debug("No override of number of events detected - defaulting to " + demoMaxEventCount + " events.");
        } else {
            demoMaxEventCount = Long.valueOf(args[0]);
        }

        Configuration config = new Configuration();
        config.addEventTypeAutoName("com.cor.cep.event");
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
        EPAdministrator epAdministrator = epService.getEPAdministrator();
        EPRuntime epRuntime = epService.getEPRuntime();

        // Register event pattern queries in the CEP and actions to be triggered on a match
        EPStatement ep = epAdministrator.createEPL(TemperatureQueries.QUERY_CRITICAL_TEMPERATURE);
        ep.setSubscriber(new TemperatureCriticalAction());

        ep = epAdministrator.createEPL(TemperatureQueries.QUERY_WARNING_TEMPERATURE);
        ep.setSubscriber(new TemperatureWarningAction());

        ep = epAdministrator.createEPL(TemperatureQueries.QUERY_TEMPERATURE_MONITOR);
        ep.setSubscriber(new TemperatureMonitorAction());

        // create an event stream and send events to the CEP
        for (int count = 0; count < demoMaxEventCount; count++) {
            // create a new temperature event
            int temperature = new Random().nextInt(500);
            Date timeOfReading = new Date();
            TemperatureEvent temperatureEvent = new TemperatureEvent(temperature, timeOfReading);

            logger.debug(temperatureEvent.toString());

            // send event to the CEP [which also makes the CEP run all queries and trigger respective actions]
            // A time window [i.e. 10s or as defined] of all events is queried in the CEP and when event patterns
            // match any above query the CEP triggers its subscribed action
            epRuntime.sendEvent(temperatureEvent);

            // This is not required. For illustration, add some delay (sleep)
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error("Thread Interrupted", e);
            }
        }

        System.out.println("Finished");
    }
}
