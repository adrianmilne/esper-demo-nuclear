package com.cor.cep.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Listens for Warnings Events being raised by the TemperatureEventHandler.
 *
 */
public class TemperatureWarningEventListener implements UpdateListener {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureWarningEventListener.class);

    /**
     * Listener method called when Esper has detected a pattern match.
     */
    @SuppressWarnings("unchecked")
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        
        Map<String, Integer> map = (Map<String, Integer>) newEvents[0].getUnderlying();
        
        int val1 = (Integer) map.get("val_1"); // 1st Temperature in the Warning Sequence
        int val2 = (Integer) map.get("val_2"); // 2nd Temperature in the Warning Sequence

        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------");
        sb.append("\n- [WARNING] : TEMPERATURE SPIKE DETECTED = " + val1 + "," + val2);
        sb.append("\n--------------------------------------------------");
        
        LOG.debug(sb.toString());
        
    }
}
