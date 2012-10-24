package com.cor.cep.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Listens for Critical Events being triggered by the TemperatureEventHandler.
 *
 */
public class TemperatureCriticalEventListener implements UpdateListener {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureCriticalEventListener.class);

    /**
     * Listener method called when Esper has detected a pattern match.
     */
    @SuppressWarnings("unchecked")
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        
        Map<String, Integer> map = (Map<String, Integer>) newEvents[0].getUnderlying();
        int val1 = (Integer) map.get("val_1"); // 1st Temperature in the Critical Sequence
        int val2 = (Integer) map.get("val_2"); // 2nd Temperature in the Critical Sequence
        int val3 = (Integer) map.get("val_3"); // 3rd Temperature in the Critical Sequence
        int val4 = (Integer) map.get("val_4"); // 4th Temperature in the Critical Sequence
        
        StringBuilder sb = new StringBuilder();
        sb.append("***************************************");
        sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
        sb.append("\n* " + val1 + " > " + val2 + " > " + val3 + " > " + val4);
        sb.append("\n***************************************");
        
        LOG.debug(sb.toString());
    }
}
