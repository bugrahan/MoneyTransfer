package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.UserApiService;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.User;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

public class UserApiServiceImpl extends UserApiService {

    @Override
    public Response createUser(String body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        DatabaseReplica.getUserHashMap().size();
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(body, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Transaction newTransaction = Transaction.getInstance(size+1, body.getFrom(), body.getTo(), body.getAmount(), body.getCurrencyCode());
        DatabaseReplica.getUserHashMap().put(user.getId(), user);


        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response deleteUser(long id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getUserById(long id, SecurityContext securityContext) throws NotFoundException {
        User returnUser = DatabaseReplica.getUserHashMap().get(id);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(returnUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
        // do some magic!
        //return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateUser(String  body, long id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
