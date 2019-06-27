package com.revolut.bugrahan.factories;

import com.revolut.bugrahan.api.UserApiService;
import com.revolut.bugrahan.impl.UserApiServiceImpl;

public class UserApiServiceFactory {
    private final static UserApiService service = new UserApiServiceImpl();

    public static UserApiService getUserApi() {
        return service;
    }
}
