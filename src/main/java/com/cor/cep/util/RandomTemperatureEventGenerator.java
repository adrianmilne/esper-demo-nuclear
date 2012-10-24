package com.cor.cep.util;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cor.cep.event.TemperatureEvent;

import com.cor.cep.handler.TemperatureEventHandler;

/**
 * Just a simple class to create a number of Random TemperatureEvents and pass them off to the
 * TemperatureEventHandler.
 */
public class RandomTemperatureEventGenerator {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(RandomTemperatureEventGenerator.class);

    /** The TemperatureEventHandler - wraps the Esper engine and processes the Events  */
    @Autowired
    private TemperatureEventHandler temperatureEventHandler;

    /**
     * Creates simple random Temperature events and lets the implementation class handle them.
     */
    public void startSendingTemperatureReadings(final long noOfTemperatureEvents) {

        ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

        xrayExecutor.submit(new Runnable() {
            public void run() {

                int count = 0;
                while (count < noOfTemperatureEvents) {
                    TemperatureEvent ve = new TemperatureEvent(new Random().nextInt(500));
                    temperatureEventHandler.handle(ve);
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        LOG.error("Thread Interrupted", e);
                    }
                }

            }
        });
    }

}
