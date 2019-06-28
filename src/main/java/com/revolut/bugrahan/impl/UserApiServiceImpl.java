package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.UserApiService;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.User;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.HashMap;

public class UserApiServiceImpl extends UserApiService {

    @Override
    public Response createUser(String body, SecurityContext securityContext) throws NotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        User user;
        try {
            user = mapper.readValue(body, User.class);
        } catch (IOException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }

        HashMap<Long, User> userMap = DatabaseReplica.getUserHashMap();

        if (user == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot be created.")).build();
        } else if(user.getId() == 0 || StringUtils.isEmpty(user.getName()) || user.getUserType() == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing information.")).build();
        } else if (userMap.containsKey(user.getId())) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User already exists.")).build();
        }

        DatabaseReplica.getUserHashMap().put(user.getId(), user);
        return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "User created.")).build();
    }

    @Override
    public Response deleteUser(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getUserHashMap().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        } else {
            DatabaseReplica.getUserHashMap().remove(id);
            return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "User deleted!")).build();

        }
    }

    @Override
    public Response getUserById(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getUserHashMap().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        }
        User returnUser = DatabaseReplica.getUserHashMap().get(id);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json;
        try {
            json = ow.writeValueAsString(returnUser);
        } catch (JsonProcessingException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }
        return Response.status(200).entity(json).build();
    }

    @Override
    public Response updateUser(String  body, long id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
