package com.cor.cep.statement;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemperatureCriticalStatement {
    private static Logger LOG = LoggerFactory.getLogger(TemperatureCriticalStatement.class);

    /** Used as the minimum starting threshold for a critical event. */
    private static final Integer CRITICAL_EVENT_THRESHOLD = 100;

    /**
     * If the last event in a critical sequence is this much greater than the first - issue a
     * critical alert.
     */
    private static final Double CRITICAL_EVENT_MULTIPLIER = 1.5;

    /**
     * EPL to check for a sudden critical rise across 4 events, where the last event is 1.5x greater
     * than the first event. This is checking for a sudden, sustained escalating rise in the
     * temperature
     */
    public static EPStatement createAndSubscribe(EPServiceProvider epService, Object action) {
        LOG.debug("create Critical Temperature Check Expression");
        EPStatement ep = epService.getEPAdministrator().createEPL(
            "select * from TemperatureEvent "
                + "match_recognize ( "
                + "       measures A as temp1, B as temp2, C as temp3, D as temp4 "
                + "       pattern (A B C D) "
                + "       define "
                + "               A as A.temperature > " + CRITICAL_EVENT_THRESHOLD + ", "
                + "               B as (A.temperature < B.temperature), "
                + "               C as (B.temperature < C.temperature), "
                + "               D as (C.temperature < D.temperature) and D.temperature > (A.temperature * " + CRITICAL_EVENT_MULTIPLIER + ")" + ")"
        );
        ep.setSubscriber(action);
        return ep;
    }
}
