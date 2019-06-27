package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

public class Transaction {
    private long id;
    private long from;
    private long to;
    private double amount;
    private String currencyCode;

    @JsonCreator
    public Transaction(@JsonProperty(value = "id", required = true) long id,
                       @JsonProperty(value = "from", required = true) long from,
                       @JsonProperty(value = "to", required = true) long to,
                       @JsonProperty(value = "amount", required = true) double amount,
                       @JsonProperty(value = "currencyCode", required = true) String currencyCode) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.currencyCode = currencyCode;
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
