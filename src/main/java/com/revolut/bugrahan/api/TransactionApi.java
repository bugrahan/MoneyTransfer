package com.revolut.bugrahan.api;

import com.revolut.bugrahan.factories.TransactionApiServiceFactory;
import com.revolut.bugrahan.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/transaction")
public class TransactionApi {
    private final TransactionApiService delegate;

    public TransactionApi(@Context ServletConfig servletContext) {
        TransactionApiService delegate = null;

        if (servletContext != null) {
            String implClass = servletContext.getInitParameter("TransactionApi.implementation");
            if (implClass != null && !"".equals(implClass.trim())) {
                try {
                    delegate = (TransactionApiService) Class.forName(implClass).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (delegate == null) {
            delegate = TransactionApiServiceFactory.getTransactionApi();
        }

        this.delegate = delegate;
    }

    @POST
    @Consumes("application/json")
    @Operation(summary = "Create transaction", tags={ "transaction" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation") })
    public Response createTransaction(@Parameter(description = "Created transaction object" ,required=true) String body,
                                      @Context SecurityContext securityContext) throws NotFoundException {
        return delegate.createTransaction(body,securityContext);
    }
}

