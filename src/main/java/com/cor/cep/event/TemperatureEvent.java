package com.cor.cep.event;

import java.util.Date;
import java.util.Objects;

/**
 * Immutable Temperature Event class. The process control system creates these events. The
 * TemperatureEventHandler picks these up and processes them.
 */
public final class TemperatureEvent {

    /** Temperature in Celcius. */
    private final int temperature;
    
    /** Time temerature reading was taken. */
    private final Date timeOfReading;
    
    /**
     * Single value constructor.
     * @param value Temperature in Celsius.
     */
    /**
     * Temeratur constructor.
     * @param temperature Temperature in Celsius
     * @param timeOfReading Time of Reading
     */
    public TemperatureEvent(int temperature, Date timeOfReading) {
        this.temperature = temperature;
        Objects.requireNonNull(timeOfReading, "timeOfReading");
        this.timeOfReading = new Date(timeOfReading.getTime());
    }

    /**
     * Get the Temperature.
     * @return Temperature in Celsius
     */
    public int getTemperature() {
        return temperature;
    }
       
    /**
     * Get time Temperature reading was taken.
     * @return Time of Reading
     */
    public Date getTimeOfReading() {
        return new Date(timeOfReading.getTime());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        TemperatureEvent that = (TemperatureEvent) other;
        return temperature == that.temperature && Objects.equals(timeOfReading, that.timeOfReading);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, timeOfReading);
    }

    @Override
    public String toString() {
        return "TemperatureEvent [" + temperature + "C]";
    }

}
