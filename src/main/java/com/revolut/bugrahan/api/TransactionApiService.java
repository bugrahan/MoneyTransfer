package com.revolut.bugrahan.api;

import com.revolut.bugrahan.model.Transaction;
import com.revolut.bugrahan.model.User;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class TransactionApiService {
    public abstract Response createTransaction(String body, SecurityContext securityContext) throws NotFoundException;

    public abstract boolean isSenderAccountExists(Transaction transaction);

    public abstract boolean isReceiverAccountExists(Transaction transaction);

    public abstract boolean isTransactionCurrenciesMatch(Transaction transaction);

    public abstract boolean isTransactionCurrencyMatchToSendersAccount(Transaction transaction);

    public abstract boolean isTransactionCurrencyMatchToReceiversAccount(Transaction transaction);

    public abstract boolean hasSenderEnoughLimit(Transaction transaction);

    public abstract boolean isThereEnoughMoneyInTheSendersAccount(Transaction transaction);

    public abstract void applyTransaction(Transaction transaction);


}
