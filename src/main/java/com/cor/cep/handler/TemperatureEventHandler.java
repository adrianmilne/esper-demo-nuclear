package com.cor.cep.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cor.cep.event.TemperatureEvent;
import com.cor.cep.subscriber.StatementSubscriber;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * This class handles incoming Temperature Events. It processes them through the EPService, to which
 * it has attached the 3 queries.
 */
@Component
@Scope(value = "singleton")
public class TemperatureEventHandler implements InitializingBean{

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureEventHandler.class);

    /** Esper service */
    private EPServiceProvider epService;
    private EPStatement criticalEventStatement;
    private EPStatement warningEventStatement;
    private EPStatement monitorEventStatement;

    @Autowired
    @Qualifier("criticalEventSubscriber")
    private StatementSubscriber criticalEventSubscriber;

    @Autowired
    @Qualifier("warningEventSubscriber")
    private StatementSubscriber warningEventSubscriber;

    @Autowired
    @Qualifier("monitorEventSubscriber")
    private StatementSubscriber monitorEventSubscriber;

    /**
     * Configure Esper Statement(s).
     */
    public void initService() {

        LOG.debug("Initializing Servcie ..");
        Configuration config = new Configuration();
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
        
        LOG.debug("create Critical Temperature Check Expression");
        criticalEventStatement = epService.getEPAdministrator().createEPL(criticalEventSubscriber.getStatement());
        criticalEventStatement.setSubscriber(criticalEventSubscriber);
    }

    /**
     * EPL to check for 2 consecutive Temperature events over the threshold - if matched, will alert
     * listener.
     */
    private void createWarningTemperatureCheckExpression() {

        LOG.debug("create Warning Temperature Check Expression");
        warningEventStatement = epService.getEPAdministrator().createEPL(warningEventSubscriber.getStatement());
        warningEventStatement.setSubscriber(warningEventSubscriber);
    }

    /**
     * EPL to monitor the average temperature every 10 seconds. Will call listener on every event.
     */
    private void createTemperatureMonitorExpression() {

        LOG.debug("create Timed Average Monitor");
        monitorEventStatement = epService.getEPAdministrator().createEPL(monitorEventSubscriber.getStatement());
        monitorEventStatement.setSubscriber(monitorEventSubscriber);
    }

    /**
     * Handle the incoming TemperatureEvent.
     */
    public void handle(TemperatureEvent event) {

        LOG.debug(event.toString());
        epService.getEPRuntime().sendEvent(event);

    }

    @Override
    public void afterPropertiesSet() {
        
        LOG.debug("Configuring..");
        initService();
    }
}
