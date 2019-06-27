package com.revolut.bugrahan.factories;

import com.revolut.bugrahan.api.TransactionApiService;
import com.revolut.bugrahan.impl.TransactionApiServiceImpl;

public class TransactionApiServiceFactory {
    private final static TransactionApiService service = new TransactionApiServiceImpl();

    public static TransactionApiService getTransactionApi() {
        return service;
    }
}
