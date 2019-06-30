package com.revolut.bugrahan.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.bugrahan.api.ApiResponseMessage;
import com.revolut.bugrahan.api.NotFoundException;
import com.revolut.bugrahan.api.TransactionApiService;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.Account;
import com.revolut.bugrahan.model.Currency;
import com.revolut.bugrahan.model.Transaction;
import com.revolut.bugrahan.model.User;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

public class TransactionApiServiceImpl extends TransactionApiService {
    @Override
    public Response createTransaction(String body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        ObjectMapper mapper = new ObjectMapper();
        Transaction transaction;
        try {
            transaction = mapper.readValue(body, Transaction.class);
        } catch (IOException e) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.toString())).build();
        }

        if (checkTransaction(transaction).getStatus() == 404) {
            return checkTransaction(transaction);
        }

        applyTransaction(transaction);

        return Response.status(200).entity(new ApiResponseMessage(ApiResponseMessage.OK, "Transaction done!")).build();
    }

    private Response checkTransaction(Transaction transaction) {
        if (!isSenderAccountExists(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Sender account doesn't exist.")).build();
        } else if (!isReceiverAccountExists(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Receiver account doesn't exist.")).build();
        } else if (!isTransactionCurrencyMatchToSendersAccount(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Sender's account is not a " + transaction.getCurrencyCode() + " account.")).build();
        } else if (!isTransactionCurrencyMatchToReceiversAccount(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Receiver's account is not a " + transaction.getCurrencyCode() + " account.")).build();
        } else if (!isTransactionCurrenciesMatch(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Sender's and receiver's accounts doesn't match.")).build();
        } else if (!hasSenderEnoughLimit(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Sender doesn't have enough limit.")).build();
        } else if (!isThereEnoughMoneyInTheSendersAccount(transaction)) {
            return Response.status(404).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
                    "Sender doesn't have enough money to send.")).build();
        }

        return Response.status(200).build();
    }


    @Override
    public boolean isSenderAccountExists(Transaction transaction) {
        return DatabaseReplica.getAccountHashMap().containsKey(transaction.getSenderAccountId());
    }

    @Override
    public boolean isReceiverAccountExists(Transaction transaction) {
        return DatabaseReplica.getAccountHashMap().containsKey(transaction.getReceiverAccountId());
    }

    @Override
    public boolean isTransactionCurrenciesMatch(Transaction transaction) {
        return DatabaseReplica.getAccountHashMap().get(transaction.getSenderAccountId()).getCurrency().equals(
                DatabaseReplica.getAccountHashMap().get(transaction.getReceiverAccountId()).getCurrency());
    }

    @Override
    public boolean isTransactionCurrencyMatchToSendersAccount(Transaction transaction) {
        return DatabaseReplica.getAccountHashMap().get(transaction.getSenderAccountId()).getCurrency().getValue().equals(transaction.getCurrencyCode());
    }

    @Override
    public boolean isTransactionCurrencyMatchToReceiversAccount(Transaction transaction) {
        return DatabaseReplica.getAccountHashMap().get(transaction.getReceiverAccountId()).getCurrency().getValue().equals(transaction.getCurrencyCode());
    }

    @Override
    public boolean hasSenderEnoughLimit(Transaction transaction) {
        Account senderAccount = DatabaseReplica.getAccountHashMap().get(transaction.getSenderAccountId());
        User sender = DatabaseReplica.getUserHashMap().get(senderAccount.getOwnerId());
        double limit = sender.getRemainingTransferLimit();
        double amountInGBP = getAmountInGBP(transaction.getAmount(), transaction.getCurrencyCode());
        return limit >= amountInGBP;
    }

    @Override
    public boolean isThereEnoughMoneyInTheSendersAccount(Transaction transaction) {
        return DatabaseReplica.getAccountHashMap().get(transaction.getSenderAccountId()).getBalance() >= transaction.getAmount();
    }




    private double getAmountInGBP(double amount, String currencyCode) {
        if (currencyCode.equals(Currency.GBP.getValue())) {
            return amount;
        } else {
            return Currency.valueOf(currencyCode).getSellingRate() * amount;
        }
    }


    public void applyTransaction(Transaction transaction) {
        int size = DatabaseReplica.getTransactionHashMap().size();
        DatabaseReplica.getTransactionHashMap().put((long)size+1, transaction);
        Account senderAccount = DatabaseReplica.getAccountHashMap().get(transaction.getSenderAccountId());
        senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
        User sender = DatabaseReplica.getUserHashMap().get(senderAccount.getOwnerId());
        sender.setRemainingTransferLimit(sender.getRemainingTransferLimit() - transaction.getAmount());
        Account receiverAccount = DatabaseReplica.getAccountHashMap().get(transaction.getReceiverAccountId());
        receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
    }


}
