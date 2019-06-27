package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    @JsonProperty("id")
    private long id;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("ownerId")
    private long ownerId;

    private Account(long id, double balance, Currency currency, long ownerId) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.ownerId = ownerId;
    }

    public static Account getInstance(long id, double balance, Currency currency, long ownerId) {
        return new Account(id, balance, currency, ownerId);
    }

    public long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getOwner() {
        return ownerId;
    }

    // TODO equals

    // TODO hashCode

    // TODO toString
}
