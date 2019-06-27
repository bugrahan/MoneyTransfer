package com.revolut.bugrahan.api;

import com.revolut.bugrahan.model.Transaction;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class TransactionApiService {
    public abstract Response createTransaction(Transaction body, SecurityContext securityContext) throws NotFoundException;
}
