package com.revolut.bugrahan.factories;

import com.revolut.bugrahan.api.AccountApiService;
import com.revolut.bugrahan.impl.AccountApiServiceImpl;

public class AccountApiServiceFactory {
    private final static AccountApiService service = new AccountApiServiceImpl();

    public static AccountApiService getAccountApi() {
        return service;
    }}
