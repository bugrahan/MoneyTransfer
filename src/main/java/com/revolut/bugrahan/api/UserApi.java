package com.revolut.bugrahan.api;

import com.revolut.bugrahan.factories.UserApiServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.servlet.ServletConfig;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
public class UserApi {
    private final UserApiService delegate;

    public UserApi(@Context ServletConfig servletContext) {
        UserApiService delegate = null;

        if (servletContext != null) {
            String implClass = servletContext.getInitParameter("UserApi.implementation");
            if (implClass != null && !"".equals(implClass.trim())) {
                try {
                    delegate = (UserApiService) Class.forName(implClass).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (delegate == null) {
            delegate = UserApiServiceFactory.getUserApi();
        }

        this.delegate = delegate;
    }


    @POST

    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Create user.", description = "", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response createUser(@Parameter(description = "Created user object.", required = true) String body,
                               @Context SecurityContext securityContext) throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.createUser(body, securityContext);
    }


    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Delete user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response deleteUser(@Parameter(description = "The id that needs to be deleted", required = true) @PathParam("id") long id,
                               @Context SecurityContext securityContext) throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.deleteUser(id, securityContext);
    }

    @GET
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Get user by user id", description = "", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response getUserById(@Parameter(description = "The id that needs to be fetched.", required = true) @PathParam("id") long id,
                                @Context SecurityContext securityContext) throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.getUserById(id, securityContext);
    }

    @GET
    @Path("/{id}/{accountId}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Get user's account by account id", description = "", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response getAccountById(@Parameter(description = "User id", required = true) @PathParam("id") long id,
                                   @Parameter(description = "Account id", required = true) @PathParam("accountId") long accountId,
                                   @Context SecurityContext securityContext) throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.getAccountById(id, accountId, securityContext);
    }


    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Updated user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "Failed operation.")})
    public Response updateUser(@Parameter(description = "Updated user object", required = true) String body,
                               @Parameter(description = "The id that need to be updated.", required = true) @PathParam("id") long id,
                               @Context SecurityContext securityContext) throws NotFoundException {
        return delegate.updateUser(body, id, securityContext);
    }
}
