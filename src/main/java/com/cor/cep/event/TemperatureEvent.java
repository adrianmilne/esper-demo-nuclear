package com.cor.cep.event;

import java.util.Date;

/**
 * Immutable Temperature Event class. The process control system creates these events. The
 * TemperatureEventHandler picks these up and processes them.
 */
public class TemperatureEvent {

    /** Temperature in Celcius. */
    private int temperature;
    
    /** Time temerature reading was taken. */
    private Date timeOfReading;
    
    /**
     * Single value constructor.
     * @param value Temperature in Celsius.
     */
    public TemperatureEvent(int temperature, Date timeOfReading) {
        this.temperature = temperature;
        this.timeOfReading = timeOfReading;
    }

    /**
     * Get the Temperature.
     * @return Temperature in Celsius.
     */
    public int getValue() {
        return temperature;
    }

    @Override
    public String toString() {
        return "TemperatureEvent [temperature=" + temperature + ", timeOfReading=" + timeOfReading + "]";
    }
    
    
}
