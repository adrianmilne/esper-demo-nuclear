package com.cor.cep.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains the action triggered when EPL Query matches. No dependency on Esper libraries.
 */
public class TemperatureMonitorAction {
    private static Logger LOG = LoggerFactory.getLogger(TemperatureMonitorAction.class);

    public void update(Map<String, Double> eventMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------");
        sb.append("\n- [MONITOR] Average Temp = ");
        sb.append(eventMap.get("avg_val")); // average temp over 10 secs
        sb.append("\n---------------------------------");
        LOG.debug(sb.toString());
    }
}
