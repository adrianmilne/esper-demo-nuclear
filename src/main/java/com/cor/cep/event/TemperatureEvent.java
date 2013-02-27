package com.cor.cep.event;

/**
 * Immutable Temperature Event class. The process control system creates these events. The
 * TemperatureEventHandler picks these up and processes them.
 */
public class TemperatureEvent {

    private int value;

    public TemperatureEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TemperatureEvent [" + value + "C]";
    }

}
