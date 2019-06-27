package com.revolut.bugrahan.impl;

import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.TransactionApiService;
import com.revolut.bugrahan.model.Transaction;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class TransactionApiServiceImpl extends TransactionApiService {
    @Override
    public Response createTransaction(Transaction body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
