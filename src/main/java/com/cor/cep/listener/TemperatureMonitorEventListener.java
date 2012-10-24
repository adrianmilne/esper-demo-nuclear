package com.cor.cep.listener;

import java.text.DecimalFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Listens for Monitor data being released by the TemperatureEventHandler.
 *
 */
public class TemperatureMonitorEventListener implements UpdateListener {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureMonitorEventListener.class);

    /**
     * Listener method called when Esper has detected a pattern match.
     */
    @SuppressWarnings("unchecked")
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        
        Map<String, Double> map = (Map<String, Double>) newEvents[0].getUnderlying();
        
        Double avg = (Double) map.get("avg_val"); // average temp over 10 secs

        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------");
        sb.append("\n- [MONITOR] Average Temp = " + new DecimalFormat("#.##").format(avg) );
        sb.append("\n---------------------------------");
        
        LOG.debug(sb.toString());
        
    }
}
