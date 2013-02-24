package com.cor.cep.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.cor.cep.event.TemperatureEvent;
import com.cor.cep.listener.TemperatureCriticalEventListener;
import com.cor.cep.listener.TemperatureMonitorEventListener;
import com.cor.cep.listener.TemperatureWarningEventListener;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * This class handles incoming Temperature Events. It processes them through the EPService, to which
 * it has attached the 3 queries.
 */
public class TemperatureEventHandler implements InitializingBean {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureEventHandler.class);

    /** If 2 consecutive temperature events are greater than this - issue a warning */
    private static final String WARNING_EVENT_THRESHOLD = "400";

    /** Used as the minimum starting threshold for a critical event. */
    private static final String CRITICAL_EVENT_THRESHOLD = "100";

    /**
     * If the last event in a critical sequence is this much greater than the first - issue a
     * critical alert.
     */
    private static final String CRITICAL_EVENT_MULTIPLIER = "1.5";

    /** Esper service. */
    private EPServiceProvider epService;
    
    /** Contains the Esper Query Language statement for listening for critical events. */
    private EPStatement criticalEventStatement;
    /** Contains the Esper Query Language statement for listening for warning events. */
    private EPStatement warningEventStatement;
    /** Contains the Esper Query Language statement for listening for monitor events. */
    private EPStatement monitorEventStatement;

    /** Listener which executes when a the criticalEventStatement detects a match. */
    @Autowired
    private TemperatureCriticalEventListener criticalEventListener;
    
    /** Listener which executes when a the warningEventStatement detects a match. */
    @Autowired
    private TemperatureWarningEventListener warningEventListener;
    
    /** Listener which executes when a the monitorEventStatement detects a match. */
    @Autowired
    private TemperatureMonitorEventListener monitorEventListener;

    /**
     * Configure Esper Statement(s).
     */
    public void initService() {

        Configuration config = new Configuration();
        
        // Recognise domain objects in this package in Esper EQL statements.
        config.addEventTypeAutoName("com.cor.cep.event");
        epService = EPServiceProviderManager.getDefaultProvider(config);

        createCriticalTemperatureCheckExpression();
        createWarningTemperatureCheckExpression();
        createTemperatureMonitorExpression();
    }

    /**
     * EPL to check for a sudden critical rise across 4 events, where the last event is 1.5x greater
     * than the first event. This is checking for a sudden, sustained escalating rise in the
     * temperature
     */
    private void createCriticalTemperatureCheckExpression() {

        // Example using 'Match Recognise' syntax.
        String crtiticalEventExpression = "select * from TemperatureEvent "
                + "match_recognize ( "
                + "       measures A.value as val_1, B.value as val_2, C.value as val_3, D.value as val_4 "
                + "       pattern (A B C D) " 
                + "       define "
                + "               A as A.value > " + CRITICAL_EVENT_THRESHOLD + ", "
                + "               B as (A.value < B.value), "
                + "               C as (B.value < C.value), "
                + "               D as (C.value < D.value) and D.value > (A.value * " + CRITICAL_EVENT_MULTIPLIER + ")" + ")";

        criticalEventStatement = epService.getEPAdministrator().createEPL(crtiticalEventExpression);
        criticalEventStatement.addListener(criticalEventListener);
    }

    /**
     * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert
     * listener.
     */
    private void createWarningTemperatureCheckExpression() {

        // Example using 'Match Recognise' syntax.
        String warningEventExpression = "select * from TemperatureEvent "
                + "match_recognize ( "
                + "       measures A.value as val_1, B.value as val_2 "
                + "       pattern (A B) " 
                + "       define " 
                + "               A as A.value > " + WARNING_EVENT_THRESHOLD + ", "
                + "               B as B.value > " + WARNING_EVENT_THRESHOLD + ")";

        warningEventStatement = epService.getEPAdministrator().createEPL(warningEventExpression);
        warningEventStatement.addListener(warningEventListener);
    }

    /**
     * EPL to monitor the average temperature every 10 seconds. Will call listener on every event.
     */
    private void createTemperatureMonitorExpression() {

        // Example of simple EPL with a Time Window of 10 seconds
        String monitorEventExpression = "select avg(value) as avg_val from TemperatureEvent.win:time_batch(10 sec)";
        monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventExpression);
        monitorEventStatement.addListener(monitorEventListener);
    }

    /**
     * Handle the incoming TemperatureEvent.
     */
    public void handle(TemperatureEvent event) {

        LOG.debug(event.toString());

        epService.getEPRuntime().sendEvent(event);

    }

    /**
     * Auto initialise our service after Spring bean wiring is complete.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initService();
    }
}
