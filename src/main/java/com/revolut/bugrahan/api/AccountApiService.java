package com.revolut.bugrahan.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class AccountApiService {
    public abstract Response createAccount(String body, SecurityContext securityContext) throws NotFoundException;

    public abstract Response deleteAccount(long id, SecurityContext securityContext) throws NotFoundException;

    public abstract Response getAccountById(long id, SecurityContext securityContext) throws NotFoundException;

    public abstract Response updateAccount(String body, long id, SecurityContext securityContext) throws NotFoundException;
}
