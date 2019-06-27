package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Double.compare(account.balance, balance) == 0 &&
                ownerId == account.ownerId &&
                currency == account.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, currency, ownerId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", currency=" + currency +
                ", ownerId=" + ownerId +
                '}';
    }
}
