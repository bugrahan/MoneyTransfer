package com.revolut.bugrahan.api;

import com.revolut.bugrahan.factories.AccountApiServiceFactory;
import com.revolut.bugrahan.impl.AccountApiServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.servlet.ServletConfig;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@Path("/account")
public class AccountApi {
    private final AccountApiService delegate;

    public AccountApi(@Context ServletConfig servletContext) {
        AccountApiService delegate = null;

        if (servletContext != null) {
            String implClass = servletContext.getInitParameter("AccountApi.implementation");
            if (implClass != null && !"".equals(implClass.trim())) {
                try {
                    delegate = (AccountApiServiceImpl) Class.forName(implClass).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (delegate == null) {
            delegate = AccountApiServiceFactory.getAccountApi();
        }

        this.delegate = delegate;
    }


    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Create account.", tags = {"account"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response createAccount(@Parameter(description = "Created account object.", required = true) String body,
                                  @Context SecurityContext securityContext) throws NotFoundException {
        return delegate.createAccount(body, securityContext);
    }


    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Delete account.", tags = {"account"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response deleteAccount(@Parameter(description = "The id that needs to be deleted.", required = true) @PathParam("id") long id,
                                  @Context SecurityContext securityContext) throws NotFoundException {
        return delegate.deleteAccount(id, securityContext);
    }


    @GET
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Get account by id.", tags = {"account"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response getAccountById(@Parameter(description = "The id that needs to be fetched.", required = true) @PathParam("id") long id,
                                   @Context SecurityContext securityContext) throws NotFoundException {
        return delegate.getAccountById(id, securityContext);
    }


    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Update account.", tags = {"account"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response updateAccount(@Parameter(description = "Updated account object.", required = true) String body,
                                  @Parameter(description = "The id that needs to be updated.", required = true) @PathParam("id") long id,
                                  @Context SecurityContext securityContext) throws NotFoundException {
        return delegate.updateAccount(body, id, securityContext);
    }
}