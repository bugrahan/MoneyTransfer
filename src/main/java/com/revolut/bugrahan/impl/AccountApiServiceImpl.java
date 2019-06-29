package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revolut.bugrahan.api.AccountApiService;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.Account;
import com.revolut.bugrahan.model.User;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.HashMap;

public class AccountApiServiceImpl extends AccountApiService {

    @Override
    public Response createAccount(String  body, SecurityContext securityContext) throws NotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = null;
        try {
            account = mapper.readValue(body, Account.class);
        } catch (IOException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }

        HashMap<Long, User> userMap = DatabaseReplica.getUserHashMap();
        HashMap<Long, Account> accountMap = DatabaseReplica.getAccountHashMap();

        if (account == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot be created.")).build();
        } else if(account.getId() == 0 || account.getOwnerId() == 0 || account.getBalance() <= 0 || account.getCurrency() == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing information.")).build();
        } else if (accountMap.containsKey(account.getId())) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account already exists.")).build();
        } else if (!userMap.containsKey(account.getOwnerId())) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        }


        DatabaseReplica.getAccountHashMap().put(account.getId(), account);
        DatabaseReplica.getUserHashMap().get(account.getOwnerId()).addAccountIdToAccountIdList(account.getId());
        return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "Account created.")).build();
    }
    @Override
    public Response deleteAccount(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getAccountHashMap().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot found.")).build();
        } else {
            long ownerId = DatabaseReplica.getAccountHashMap().get(id).getOwnerId();
            DatabaseReplica.getAccountHashMap().remove(id);

            DatabaseReplica.getUserHashMap().get(ownerId).deleteAccountIdFromAccountIdList(id);
            return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "Account deleted!")).build();

        }
    }
    @Override
    public Response getAccountById(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getAccountHashMap().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot found.")).build();
        }
        Account account = DatabaseReplica.getAccountHashMap().get(id);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json;
        try {
            json = ow.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }
        return Response.status(200).entity(json).build();
    }
    @Override
    public Response updateAccount(String body, long id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
