package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.bugrahan.utlis.Util;
import com.revolut.bugrahan.api.AccountApiService;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.Account;
import com.revolut.bugrahan.model.Currency;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountApiServiceImpl extends AccountApiService {

    @Override
    public synchronized Response createAccount(String  body, SecurityContext securityContext) throws NotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = null;
        try {
            account = mapper.readValue(body, Account.class);
        } catch (IOException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }


        if (account == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot be created.")).build();
        } else if(account.getId() == 0 || account.getOwnerId() == 0 || account.getBalance() <= 0 || account.getCurrency() == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Missing information.")).build();
        } else if (DatabaseReplica.getAccountHashtable().containsKey(account.getId())) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account already exists.")).build();
        } else if (!DatabaseReplica.getUserHashtable().containsKey(account.getOwnerId())) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User cannot found.")).build();
        }


        DatabaseReplica.getAccountHashtable().put(account.getId(), account);
        DatabaseReplica.getUserHashtable().get(account.getOwnerId()).addAccountIdsToAccountIdList(account.getId());
        return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "Account created.")).build();
    }
    @Override
    public synchronized Response deleteAccount(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getAccountHashtable().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot found.")).build();
        } else {
            Account accountWillDelete = DatabaseReplica.getAccountHashtable().get(id);
            if (accountWillDelete.getBalance() != 0) {
                return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account with money in it cannot be deleted.")).build();
            }
            DatabaseReplica.getAccountHashtable().remove(id);
            DatabaseReplica.getUserHashtable().get(accountWillDelete.getOwnerId()).deleteAccountIdFromAccountIdList(id);
            return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "Account deleted!")).build();

        }
    }
    @Override
    public Response getAccountById(long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getAccountHashtable().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot found.")).build();
        }

        try {
            String json = Util.objectToJson(DatabaseReplica.getAccountHashtable().get(id));
            return Response.status(200).entity(json).build();
        } catch (JsonProcessingException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }
    }
    @Override
    public synchronized Response updateAccount(String body, long id, SecurityContext securityContext) throws NotFoundException {
        if (!DatabaseReplica.getAccountHashtable().containsKey(id)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot found.")).build();
        }

        HashMap<String, Object> accountWithNewFields;
        try {
            accountWithNewFields = Util.jsonToHashMap(body);
        } catch (IOException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }

        if (accountWithNewFields == null) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account cannot be updated.")).build();
        } else if (accountWithNewFields.containsKey("id") && Long.parseLong(accountWithNewFields.get("id").toString()) != id) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Account id cannot be updated.")).build();
        } else if (accountWithNewFields.containsKey("currency") &&
                !accountWithNewFields.get("currency").equals(DatabaseReplica.getAccountHashtable().get(id).getCurrency()) &&
                hasUserAlreadySameCurrencyAccount(id, accountWithNewFields.get("currency"))) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "User already has a " + accountWithNewFields.get("currency") + " account.")).build();
        }


        Account updatedAccount = DatabaseReplica.getAccountHashtable().get(id);

        for (Map.Entry<String, Object> stringObjectEntry : accountWithNewFields.entrySet()) {
            if (stringObjectEntry.getKey().equals("balance")) {
                updatedAccount.setBalance(Double.valueOf(stringObjectEntry.getValue().toString()));
            } else if (stringObjectEntry.getKey().equals("currency")) {
                updatedAccount.setCurrency(Currency.valueOf(stringObjectEntry.getValue().toString()));
            } else if (stringObjectEntry.getKey().equals("ownerId")) {
                updatedAccount.setOwnerId(Long.valueOf(stringObjectEntry.getValue().toString()));
            }
        }
        DatabaseReplica.getAccountHashtable().put(updatedAccount.getId(), updatedAccount);
        return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "Account updated.")).build();
    }

    private boolean hasUserAlreadySameCurrencyAccount(long id, Object currency) {
        long ownerId = DatabaseReplica.getAccountHashtable().get(id).getOwnerId();
        List<String> accountCurrencyList = DatabaseReplica.getUserHashtable().get(ownerId).getAccountIdList().stream()
                .map(accountId -> DatabaseReplica.getAccountHashtable().get(accountId).getCurrency().getValue())
                .collect(Collectors.toList());

        return accountCurrencyList.contains(currency.toString()) && !DatabaseReplica.getAccountHashtable().get(id).getCurrency().equals(currency);
    }

}
