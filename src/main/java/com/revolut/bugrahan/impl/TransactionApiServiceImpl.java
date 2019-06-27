package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.TransactionApiService;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.Transaction;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

public class TransactionApiServiceImpl extends TransactionApiService {
    @Override
    public Response createTransaction(String body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        int size = DatabaseReplica.getTransactionHashMap().size();
        ObjectMapper mapper = new ObjectMapper();
        Transaction transaction = null;
        try {
            transaction = mapper.readValue(body, Transaction.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Transaction newTransaction = Transaction.getInstance(size+1, body.getFrom(), body.getTo(), body.getAmount(), body.getCurrencyCode());
        DatabaseReplica.getTransactionHashMap().put((long)size+1, transaction);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
