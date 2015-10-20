package com.cor.cep;

import com.cor.cep.action.TemperatureCriticalAction;
import com.cor.cep.action.TemperatureMonitorAction;
import com.cor.cep.action.TemperatureWarningAction;
import com.cor.cep.event.TemperatureEvent;
import com.cor.cep.statement.TemperatureCriticalStatement;
import com.cor.cep.statement.TemperatureMonitorStatement;
import com.cor.cep.statement.TemperatureWarningStatement;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

/**
 * Entry point for the Demo. Run this from your IDE, or from the command line using 'mvn exec:java'.
 */
public class EsperDemoNuclear {
    private static Logger LOG = LoggerFactory.getLogger(EsperDemoNuclear.class);

    public static void main(final String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - TEMPERATURES ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        LOG.debug(sb.toString());

        LOG.debug("Configuring..");
        Configuration config = new Configuration();
        config.addEventTypeAutoName("com.cor.cep.event");

        long numTemperatureEvents = 1000;
        if (args.length != 1) {
            LOG.debug("No override of number of events detected - defaulting to " + numTemperatureEvents + " events.");
        } else {
            numTemperatureEvents = Long.valueOf(args[0]);
        }

        LOG.debug("Initializing the Complex Event Processing Service ..");
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

        LOG.debug("Registering Event Processing statements and actions to be triggered on matching data");
        TemperatureCriticalStatement.createAndSubscribe(epService, new TemperatureCriticalAction());
        TemperatureWarningStatement.createAndSubscribe(epService, new TemperatureWarningAction());
        TemperatureMonitorStatement.createAndSubscribe(epService, new TemperatureMonitorAction());

        for (int count = 0; count < numTemperatureEvents; count++) {
            TemperatureEvent temperatureEvent = new TemperatureEvent(new Random().nextInt(500), new Date());
            LOG.debug(temperatureEvent.toString());
            epService.getEPRuntime().sendEvent(temperatureEvent);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOG.error("Thread Interrupted", e);
            }
        }
    }
}
