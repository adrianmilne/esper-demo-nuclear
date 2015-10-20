package com.cor.cep.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

/**
 * This class creates 3 queries and subscribes actions that will be executed when queries match.
 */
public class TemperatureWarningStatement {
    private static Logger LOG = LoggerFactory.getLogger(TemperatureWarningStatement.class);

    /** If 2 consecutive temperature events are greater than this - issue a warning */
    private static final Integer WARNING_EVENT_THRESHOLD = 400;

    /**
     * EPL to check for 2 consecutive Temperature events over the threshold - if matched, triggers action
     */
    public static EPStatement createAndSubscribe(EPServiceProvider epService, Object action) {
        LOG.debug("create Warning Temperature Check Expression");
        EPStatement ep = epService.getEPAdministrator().createEPL(
            "select * from TemperatureEvent "
                    + "match_recognize ( "
                    + "       measures A as temp1, B as temp2 "
                    + "       pattern (A B) "
                    + "       define "
                    + "               A as A.temperature > " + WARNING_EVENT_THRESHOLD + ", "
                    + "               B as B.temperature > " + WARNING_EVENT_THRESHOLD + ")"
        );
        ep.setSubscriber(action);
        return ep;
    }
}
