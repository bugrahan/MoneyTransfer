package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Account {
    private long id;
    private double balance;
    private Currency currency;
    private long ownerId;

    public Account(@JsonProperty(value = "id", required = true) long id,
                   @JsonProperty(value = "balance", required = true) double balance,
                   @JsonProperty(value = "currency", required = true) Currency currency,
                   @JsonProperty(value = "ownerId", required = true) long ownerId) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.ownerId = ownerId;
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

    public long getOwnerId() {
        return ownerId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
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
