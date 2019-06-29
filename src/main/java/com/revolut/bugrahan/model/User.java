package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {

    private long id;

    private String name;

    private ArrayList<Long> accountIdList;

    private UserType userType;

    private double remainingWithdrawLimit;

    private double remainingExchangeLimit;


    public User(@JsonProperty(value = "id", required = true) long id,
                @JsonProperty(value = "name", required = true) String name,
                @JsonProperty(value = "userType", required = true) UserType userType) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.accountIdList = new ArrayList<>();
        this.remainingWithdrawLimit = userType.getWithdrawLimit();
        this.remainingExchangeLimit = userType.getExchangeLimit();
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

    public void setAccountIdList(ArrayList<Long> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public void addAccountIdToAccountIdList(long... accountId) {
        for (long id : accountId) {
            this.getAccountIdList().add(id);
        }
    }

    public void deleteAccountIdFromAccountIdList(long accountId) {
        if (this.getAccountIdList().contains(accountId)) {
            this.getAccountIdList().remove(accountId);
        }
    }

    public UserType getUserType() {
        return userType;
    }

    public double getRemainingWithdrawLimit() {
        return remainingWithdrawLimit;
    }

    public double getRemainingExchangeLimit() {
        return remainingExchangeLimit;
    }

    public void setRemainingWithdrawLimit(double remainingWithdrawLimit) {
        this.remainingWithdrawLimit = remainingWithdrawLimit;
    }

    public void setRemainingExchangeLimit(double remainingExchangeLimit) {
        this.remainingExchangeLimit = remainingExchangeLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Double.compare(user.remainingWithdrawLimit, remainingWithdrawLimit) == 0 &&
                Double.compare(user.remainingExchangeLimit, remainingExchangeLimit) == 0 &&
                name.equals(user.name) &&
                Objects.equals(accountIdList, user.accountIdList) &&
                userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountIdList, userType, remainingWithdrawLimit, remainingExchangeLimit);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountIdList=" + accountIdList +
                ", userType=" + userType +
                ", remainingWithdrawLimit=" + remainingWithdrawLimit +
                ", remainingExchangeLimit=" + remainingExchangeLimit +
                '}';
    }
}
