package com.cor.cep.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.Date;

import org.junit.jupiter.api.Test;

class TemperatureEventTest {

    @Test
    void storesDefensiveCopyOfTimestamp() {
        Date now = new Date();
        TemperatureEvent event = new TemperatureEvent(120, now);
        Date returned = event.getTimeOfReading();

        assertNotSame(now, returned, "Time of reading should be a defensive copy.");
        assertEquals(now, returned, "Time of reading should match the provided value.");
    }
}
