package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.bugrahan.api.utlis.Util;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.UserApiService;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.User;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

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


        if (user == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot be created.")).build();
        } else if(user.getId() == 0 || StringUtils.isEmpty(user.getName()) || user.getUserType() == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing information.")).build();
        } else if (DatabaseReplica.getUserHashtable().containsKey(user.getId())) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User already exists.")).build();
        }

        DatabaseReplica.getUserHashtable().put(user.getId(), user);
        return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "User created.")).build();
    }

    @Override
    public Response deleteUser(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getUserHashtable().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        } else {
            if (isBalanceGreaterThanZero(id)) {
                return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User with balance cannot be deleted.")).build();
            }
            DatabaseReplica.getUserHashtable().remove(id);
            return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "User deleted!")).build();

        }
    }

    @Override
    public Response getUserById(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getUserHashtable().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        }
        try {
            String json = Util.objectToJson(DatabaseReplica.getUserHashtable().get(id));
            return Response.status(200).entity(json).build();
        } catch (JsonProcessingException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }
    }

    @Override
    public Response getAccountById(long id, long accountId, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getUserHashtable().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        }
        if (!DatabaseReplica.getUserHashtable().get(id).getAccountIdList().contains(accountId)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot found.")).build();
        }

        try {
            String json = Util.objectToJson(DatabaseReplica.getAccountHashtable().get(accountId));
            return Response.status(200).entity(json).build();
        } catch (JsonProcessingException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }
    }

    @Override
    public Response updateUser(String  body, long id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public boolean isBalanceGreaterThanZero(long userId) {
        for (Long accountId : DatabaseReplica.getUserHashtable().get(userId).getAccountIdList()) {
            if (DatabaseReplica.getAccountHashtable().get(accountId).getBalance() > 0) {
                return true;
            }
        }

        return false;
    }
}
