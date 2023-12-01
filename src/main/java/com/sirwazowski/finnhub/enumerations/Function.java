package com.sirwazowski.finnhub.enumerations;

public enum Function {
    //Stock Price Functions
    LIVE_QUOTE("quote");

    public final String name;

    Function(String name) {
        this.name = name;
    }
}
