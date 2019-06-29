package com.revolut.bugrahan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private long id;
    private String name;
    private ArrayList<Long> accountIdList;
    private UserType userType;
    private double remainingTransferLimit;


    public User(@JsonProperty(value = "id", required = true) long id,
                @JsonProperty(value = "name", required = true) String name,
                @JsonProperty(value = "userType", required = true) UserType userType) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.accountIdList = new ArrayList<>();
        this.remainingTransferLimit = userType.getTransferLimit();
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

    public void addAccountIdsToAccountIdList(long... accountIds) {
        for (long accountId : accountIds) {
            this.getAccountIdList().add(accountId);
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


    public double getRemainingTransferLimit() {
        return remainingTransferLimit;
    }

    public void setRemainingTransferLimit(double remainingTransferLimit) {
        this.remainingTransferLimit = remainingTransferLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userType);
    }
}
