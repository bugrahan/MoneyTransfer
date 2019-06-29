package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType implements Serializable {
    PREMIUM("Premium", 1500),
    STANDARD("Standard", 900);

    private final String value;
    private final double transferLimit;

    UserType(String value, double transferLimit) {
        this.value = value;
        this.transferLimit = transferLimit;
    }

    public String getValue() {
        return value;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "value='" + value + '\'' +
                ", transferLimit=" + transferLimit +
                '}';
    }
}
