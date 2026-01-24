package com.cor.cep.util;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cor.cep.event.TemperatureEvent;

import com.cor.cep.handler.TemperatureEventHandler;

/**
 * Just a simple class to create a number of Random TemperatureEvents and pass them off to the
 * TemperatureEventHandler.
 */
@Component
public class RandomTemperatureEventGenerator {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(RandomTemperatureEventGenerator.class);

    /** The TemperatureEventHandler - wraps the Esper engine and processes the Events  */
    @Autowired
    private TemperatureEventHandler temperatureEventHandler;

    /**
     * Creates simple random Temperature events and lets the implementation class handle them.
     */
    public Future<?> startSendingTemperatureReadings(final long noOfTemperatureEvents) {

        ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

        return xrayExecutor.submit(new Runnable() {
            public void run() {

                try {
                    LOG.debug(getStartingMessage());

                    int count = 0;
                    while (count < noOfTemperatureEvents) {
                        TemperatureEvent ve = new TemperatureEvent(ThreadLocalRandom.current().nextInt(500), new Date());
                        temperatureEventHandler.handle(ve);
                        count++;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            LOG.warn("Thread interrupted while generating temperature events.", e);
                            break;
                        }
                    }
                } finally {
                    xrayExecutor.shutdown();
                }

            }
        });
    }

    
    private String getStartingMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - TEMPERATURES ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        return sb.toString();
    }
}
