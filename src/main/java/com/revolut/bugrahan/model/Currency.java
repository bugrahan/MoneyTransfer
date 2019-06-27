package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Currency implements Serializable {
    GBP("GBP", 1.00, 1.02),
    EUR("EUR", 2.01, 2.03),
    TRY("TRY", 7.50, 7.55);

    private final String value;
    private final double buyingRate;
    private final double sellingRate;

    Currency(String value, double buyingRate, double sellingRate) {
        this.value = value;
        this.buyingRate = buyingRate;
        this.sellingRate = sellingRate;
    }

    public String getValue() {
        return value;
    }

    public double getBuyingRate() {
        return buyingRate;
    }

    public double getSellingRate() {
        return sellingRate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "value='" + value + '\'' +
                ", buyingRate=" + buyingRate +
                ", sellingRate=" + sellingRate +
                '}';
    }

}
