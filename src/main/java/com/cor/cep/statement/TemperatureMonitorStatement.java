package com.cor.cep.statement;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemperatureMonitorStatement {
    private static Logger LOG = LoggerFactory.getLogger(TemperatureMonitorStatement.class);

    /**
     * EPL to monitor the average temperature every 10 seconds. Triggers action on every event.
     */
    public static EPStatement createAndSubscribe(EPServiceProvider epService, Object action) {
        LOG.debug("create Timed Average Monitor");
        EPStatement ep = epService.getEPAdministrator().createEPL(
            "select avg(temperature) as avg_val from TemperatureEvent.win:time_batch(5 sec)"
        );
        ep.setSubscriber(action);
        return ep;
    }

}
