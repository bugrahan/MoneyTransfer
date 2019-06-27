package com.revolut.bugrahan.api;

import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.UserApiService;
import com.revolut.bugrahan.factories.UserApiServiceFactory;
import com.revolut.bugrahan.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.servlet.ServletConfig;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
public class UserApi  {
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

    @Consumes({ "application/json" })

    @Operation(summary = "Create user.", description = "", tags={ "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation.") })
    public Response createUser(@Parameter(description = "Created user object." ,required=true) User body

            , @Context SecurityContext securityContext)
            throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.createUser(body,securityContext);
    }



    @DELETE
    @Path("/{id}")


    @Operation(summary = "Delete user", description = "This can only be done by the logged in user.", tags={ "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied"),

            @ApiResponse(responseCode = "404", description = "User not found") })
    public Response deleteUser(@Parameter(description = "The name that needs to be deleted",required=true) @PathParam("id") long id
            , @Context SecurityContext securityContext)
            throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.deleteUser(id,securityContext);
    }
    @GET
    @Path("/{id}")

    @Produces({ "application/json" })
    @Operation(summary = "Get user by user id", description = "", tags={ "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),

            @ApiResponse(responseCode = "400", description = "Invalid user id supplied"),

            @ApiResponse(responseCode = "404", description = "User not found") })
    public Response getUserById(@Parameter(description = "The name that needs to be fetched. Use user1 for testing. ",required=true) @PathParam("id") long id
            , @Context SecurityContext securityContext)
            throws com.revolut.bugrahan.api.NotFoundException {
        return delegate.getUserById(id,securityContext);
    }
    @PUT
    @Path("/{id}")
    @Consumes({ "*/*" })

    @Operation(summary = "Updated user", description = "xxx", tags={ "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid user supplied"),

            @ApiResponse(responseCode = "404", description = "User not found") })
    public Response updateUser(@Parameter(description = "Updated user object" ,required=true) User body

            , @Parameter(description = "name that need to be updated",required=true) @PathParam("id") Long id
            , @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.updateUser(body,id,securityContext);
    }
}
