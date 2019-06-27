package com.revolut.bugrahan.api;

import com.revolut.bugrahan.model.Account;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class AccountApiService {
    public abstract Response createAccount(Account body, SecurityContext securityContext) throws NotFoundException;

    public abstract Response deleteAccount(long id, SecurityContext securityContext) throws NotFoundException;

    public abstract Response getAccountById(long id, SecurityContext securityContext) throws NotFoundException;

    public abstract Response updateAccount(Account body, Long id, SecurityContext securityContext) throws NotFoundException;
}
