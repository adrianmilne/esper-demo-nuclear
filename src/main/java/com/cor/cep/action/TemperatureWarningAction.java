package com.cor.cep.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cor.cep.event.TemperatureEvent;

/**
 * Contains the action triggered when EPL Query matches. No dependency on Esper libraries.
 */
public class TemperatureWarningAction {
    private static Logger LOG = LoggerFactory.getLogger(TemperatureWarningAction.class);

    public void update(Map<String, TemperatureEvent> eventMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------");
        sb.append("\n- [WARNING] : TEMPERATURE SPIKE DETECTED = ");
        sb.append(eventMap.get("temp1")); // 1st Temperature in the Warning Sequence
        sb.append(",");
        sb.append(eventMap.get("temp2")); // 2nd Temperature in the Warning Sequence
        sb.append("\n--------------------------------------------------");
        LOG.debug(sb.toString());
    }
}
