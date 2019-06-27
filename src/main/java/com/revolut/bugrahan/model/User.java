package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class User {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("accountIdList")
    private List<Long> accountIdList;

    @JsonProperty("userType")
    private UserType userType;

    @JsonProperty("remainingWithdrawLimit")
    private double remainingWithdrawLimit;

    @JsonProperty("remainingExchangeLimit")
    private double remainingExchangeLimit;


    private User(long id, String name, UserType userType, double remainingWithdrawLimit, double remainingExchangeLimit) {
        this.id = id;
        this.name = name;
        this.accountIdList = new ArrayList<>();
        this.userType = userType;
        this.remainingWithdrawLimit = remainingWithdrawLimit;
        this.remainingExchangeLimit = remainingExchangeLimit;
    }


    public static User getInstance(long id, String name, UserType userType, double remainingWithdrawLimit, double remainingExchangeLimit) {
        return new User(id, name, userType, remainingWithdrawLimit, remainingExchangeLimit);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<Long> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public UserType getAccountType() {
        return userType;
    }

    public double getRemainingWithdrawLimit() {
        return remainingWithdrawLimit;
    }

    public double getRemainingExchangeLimit() {
        return remainingExchangeLimit;
    }

    // TODO equals

    // TODO hashCode

    // TODO toString
}
