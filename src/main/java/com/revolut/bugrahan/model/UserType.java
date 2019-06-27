package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType implements Serializable {
    PREMIUM("Premium", 1500, 5000),
    STANDARD("Standard", 900, 3000);

    private final String value;
    private final double withdrawLimit;
    private final double exchangeLimit;

    UserType(String value, double withdrawLimit, double exchangeLimit) {
        this.value = value;
        this.withdrawLimit = withdrawLimit;
        this.exchangeLimit = exchangeLimit;
    }

    public String getValue() {
        return value;
    }

    public double getWithdrawLimit() {
        return withdrawLimit;
    }

    public double getExchangeLimit() {
        return exchangeLimit;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "value='" + value + '\'' +
                ", withdrawLimit=" + withdrawLimit +
                ", exchangeLimit=" + exchangeLimit +
                '}';
    }
}
