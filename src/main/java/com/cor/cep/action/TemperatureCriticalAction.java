package com.cor.cep.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cor.cep.event.TemperatureEvent;

/**
 * Contains the action triggered when EPL Query matches. No dependency on Esper libraries.
 */
public class TemperatureCriticalAction {
    private static Logger LOG = LoggerFactory.getLogger(TemperatureCriticalAction.class);

    public void update(Map<String, TemperatureEvent> eventMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("***************************************");
        sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
        sb.append("\n* ");
        sb.append(eventMap.get("temp1"));
        sb.append(" > ");
        sb.append(eventMap.get("temp2"));
        sb.append(" > ");
        sb.append(eventMap.get("temp3"));
        sb.append(" > ");
        sb.append(eventMap.get("temp4"));
        sb.append("\n***************************************");
        LOG.debug(sb.toString());
    }
}
