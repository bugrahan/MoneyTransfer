package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Transaction {
    @JsonProperty("id")
    private long id;

    @JsonProperty("from")
    private long from;

    @JsonProperty("to")
    private long to;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("currencyCode")
    private String currencyCode;

    private Transaction(long id, long from, long to, double amount, String currencyCode) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public static Transaction getInstance(long id, long from, long to, double amount, String currency) {
        return new Transaction(id, from, to, amount, currency);
    }

    public long getId() {
        return id;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id &&
                from == that.from &&
                to == that.to &&
                Double.compare(that.amount, amount) == 0 &&
                currencyCode.equals(that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, amount, currencyCode);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
