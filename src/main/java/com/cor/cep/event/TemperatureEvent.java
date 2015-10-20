package com.cor.cep.event;

import java.util.Date;

public class TemperatureEvent {
    private int temperature;
    
    private Date timeOfReading;
    
    public TemperatureEvent(int temperature, Date timeOfReading) {
        this.temperature = temperature;
        this.timeOfReading = timeOfReading;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Date getTimeOfReading() {
        return timeOfReading;
    }

    public void setTimeOfReading(Date timeOfReading) {
        this.timeOfReading = timeOfReading;
    }

    @Override
    public String toString() {
        return "TemperatureEvent [" + temperature + "C]";
    }
}
