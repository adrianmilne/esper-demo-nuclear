package com.cor.cep.query;

public class TemperatureQueries {
    /**
     * Used as the minimum starting threshold for a critical event.
     */
    private static final int CRITICAL_EVENT_THRESHOLD = 100;

    /**
     * If the last event in a critical sequence is this much greater than the first - issue a
     * critical alert.
     */
    private static final double CRITICAL_EVENT_MULTIPLIER = 1.5;

    /**
     * If 2 consecutive temperature events are greater than this - issue a warning
     */
    private static final int WARNING_EVENT_THRESHOLD = 400;

    /**
     * Event pattern to check for a sudden critical rise across 4 events, where the last event is 1.5x greater
     * than the first event. This is checking for a sudden, sustained escalating rise in the
     * temperature
     */
    public static final String QUERY_CRITICAL_TEMPERATURE = "select * from TemperatureEvent "
        + "match_recognize ( "
        + "       measures A as temp1, B as temp2, C as temp3, D as temp4 "
        + "       pattern (A B C D) "
        + "       define "
        + "               A as A.temperature > " + CRITICAL_EVENT_THRESHOLD + ", "
        + "               B as (A.temperature < B.temperature), "
        + "               C as (B.temperature < C.temperature), "
        + "               D as (C.temperature < D.temperature) and D.temperature > (A.temperature * " + CRITICAL_EVENT_MULTIPLIER + ")" + ")";


    /**
     * Event pattern to monitor the average temperature every 10 seconds. Triggers action on every event.
     */
    public static final String QUERY_TEMPERATURE_MONITOR =
        "select avg(temperature) as avg_val from TemperatureEvent.win:time_batch(5 sec)";

    /**
     * Event pattern to check for 2 consecutive Temperature events over the threshold - if matched, triggers action
     */
    public static final String QUERY_WARNING_TEMPERATURE =
        "select * from TemperatureEvent "
        + "match_recognize ( "
        + "       measures A as temp1, B as temp2 "
        + "       pattern (A B) "
        + "       define "
        + "               A as A.temperature > " + WARNING_EVENT_THRESHOLD + ", "
        + "               B as B.temperature > " + WARNING_EVENT_THRESHOLD + ")";
}
