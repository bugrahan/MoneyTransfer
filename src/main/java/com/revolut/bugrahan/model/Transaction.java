package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;

import java.util.Objects;

public class Transaction {
    private long id;
    private long senderAccountId;
    private long receiverAccountId;
    private double amount;
    private String currencyCode;

    @JsonCreator
    public Transaction(@JsonProperty(value = "senderAccountId", required = true) long senderAccountId,
                       @JsonProperty(value = "receiverAccountId", required = true) long receiverAccountId,
                       @JsonProperty(value = "amount", required = true) double amount,
                       @JsonProperty(value = "currencyCode", required = true) String currencyCode) {
        this.id = DatabaseReplica.getTransactionHashtable().size() + 1;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }


    public long getId() {
        return id;
    }

    public long getSenderAccountId() {
        return senderAccountId;
    }

    public long getReceiverAccountId() {
        return receiverAccountId;
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
                senderAccountId == that.senderAccountId &&
                receiverAccountId == that.receiverAccountId &&
                currencyCode.equals(that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderAccountId, receiverAccountId, currencyCode);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderAccountId=" + senderAccountId +
                ", receiverAccountId=" + receiverAccountId +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
