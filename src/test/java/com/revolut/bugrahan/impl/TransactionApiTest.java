package com.revolut.bugrahan.impl;

import com.revolut.bugrahan.Main;
import com.revolut.bugrahan.api.TransactionApiService;
import com.revolut.bugrahan.dbReplicas.DatabaseReplica;
import com.revolut.bugrahan.model.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import static org.junit.Assert.*;

public class TransactionApiTest extends JerseyTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    @Override
    public void setUp() throws Exception {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        server.shutdownNow();
    }


    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(UserApiServiceImpl.class);
    }

    private TransactionApiService service = new TransactionApiServiceImpl();

    static {
        User joey = new User(7001, "Joey Tribbiani", UserType.STANDARD);
        User chandler = new User(7002, "Chandler Bing", UserType.PREMIUM);
        User rachel = new User(7003, "Rachel Green", UserType.STANDARD);

        Account jo_eur = new Account(12011, 100, Currency.EUR, 7001);
        Account jo_gbp = new Account(12012, 50, Currency.EUR, 7001);
        joey.addAccountIdsToAccountIdList(12011, 12012);

        Account ch_eur = new Account(12021, 13000, Currency.EUR, 7002);
        Account ch_gbp = new Account(12022, 4000, Currency.GBP, 7002);
        Account ch_try = new Account(12023, 1000, Currency.TRY, 7002);
        chandler.addAccountIdsToAccountIdList(12011, 12012, 12013);

        Account ra_gbp = new Account(12031, 200, Currency.GBP, 7003);
        Account ra_try = new Account(12032, 0, Currency.TRY, 7003);
        rachel.addAccountIdsToAccountIdList(12031, 12032);

        DatabaseReplica.getAccountHashtable().put(jo_eur.getId(), jo_eur);
        DatabaseReplica.getAccountHashtable().put(ch_eur.getId(), ch_eur);
        DatabaseReplica.getAccountHashtable().put(ch_gbp.getId(), ch_gbp);
        DatabaseReplica.getAccountHashtable().put(ch_try.getId(), ch_try);
        DatabaseReplica.getAccountHashtable().put(ra_gbp.getId(), ra_gbp);
        DatabaseReplica.getAccountHashtable().put(ra_try.getId(), ra_try);

        DatabaseReplica.getUserHashtable().put(joey.getId(), joey);
        DatabaseReplica.getUserHashtable().put(chandler.getId(), chandler);
        DatabaseReplica.getUserHashtable().put(rachel.getId(), rachel);
    }

    @Test
    public void isSenderAccountExists_thereIsSuchAccount() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "EUR");
        assertTrue(service.isSenderAccountExists(transaction));
    }

    @Test
    public void isSenderAccountExists_thereIsNoSuchAccount() {
        Transaction transaction = new Transaction(0, 12011, 10000, "EUR");
        assertFalse(service.isSenderAccountExists(transaction));
    }


    @Test
    public void isReceiverAccountExists_thereIsSuchAccount() {
        Transaction transaction = new Transaction(12021, 0, 10000, "EUR");
        assertFalse(service.isReceiverAccountExists(transaction));
    }

    @Test
    public void isReceiverAccountExists_thereIsNoSuchAccount() {
        Transaction transaction = new Transaction(12021, 0, 10000, "EUR");
        assertFalse(service.isReceiverAccountExists(transaction));
    }

    @Test
    public void isTransactionValid_senderDoesntHaveEnoughLimit() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "EUR");
        assertFalse(service.hasSenderEnoughLimit(transaction));
    }

    @Test
    public void isTransactionCurrenciesMatch_matches() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "EUR");
        assertTrue(service.isTransactionCurrenciesMatch(transaction));
    }

    @Test
    public void isTransactionCurrenciesMatch_doesnt_match() {
        Transaction transaction = new Transaction(12022, 12011, 10000, "EUR");
        assertFalse(service.isTransactionCurrenciesMatch(transaction));
    }

    @Test
    public void isTransactionCurrencyMatchToSendersAccount_matches() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "EUR");
        assertTrue(service.isTransactionCurrencyMatchToSendersAccount(transaction));
    }

    @Test
    public void isTransactionCurrencyMatchToSendersAccount_doesnt_match() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "GBP");
        assertFalse(service.isTransactionCurrencyMatchToSendersAccount(transaction));
    }

    @Test
    public void isTransactionCurrencyMatchToReceiversAccount_matches() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "EUR");
        assertTrue(service.isTransactionCurrencyMatchToSendersAccount(transaction));
    }

    @Test
    public void isTransactionCurrencyMatchToReceiversAccount_doesnt_match() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "GBP");
        assertFalse(service.isTransactionCurrencyMatchToSendersAccount(transaction));
    }

    @Test
    public void hasSenderEnoughLimit_yes() {
        Transaction transaction = new Transaction(12022, 12012, 100, "EUR");
        assertTrue(service.hasSenderEnoughLimit(transaction));
    }

    @Test
    public void hasSenderEnoughLimit_no() {
        Transaction transaction = new Transaction(12022, 12012, 5000, "EUR");
        assertFalse(service.hasSenderEnoughLimit(transaction));
    }


    @Test
    public void isThereEnoughMoneyInTheSendersAccount_yes() {
        Transaction transaction = new Transaction(12021, 12011, 10000, "EUR");
        assertTrue(service.isThereEnoughMoneyInTheSendersAccount(transaction));
    }

    @Test
    public void isThereEnoughMoneyInTheSendersAccount_no() {
        Transaction transaction = new Transaction(12011, 12021, 10000, "EUR");
        assertFalse(service.isThereEnoughMoneyInTheSendersAccount(transaction));
    }

    @Test
    public void applyTransaction() {
        Transaction transaction = new Transaction(12021, 12011, 1000, "EUR");
        double chandlersBalance = DatabaseReplica.getAccountHashtable().get(transaction.getSenderAccountId()).getBalance();
        double joeysBalance = DatabaseReplica.getAccountHashtable().get(transaction.getReceiverAccountId()).getBalance();
        double chandlersLimit = DatabaseReplica.getUserHashtable().get(DatabaseReplica.getAccountHashtable().get(transaction.getSenderAccountId()).getOwnerId()).getRemainingTransferLimit();
        service.applyTransaction(transaction);
        double chandlersNewBalance = DatabaseReplica.getAccountHashtable().get(transaction.getSenderAccountId()).getBalance();
        double joeysNewBalance = DatabaseReplica.getAccountHashtable().get(transaction.getReceiverAccountId()).getBalance();
        double chandlersNewLimit = DatabaseReplica.getUserHashtable().get(DatabaseReplica.getAccountHashtable().get(transaction.getSenderAccountId()).getOwnerId()).getRemainingTransferLimit();
        assertEquals(chandlersBalance - 1000, chandlersNewBalance, 0.001);
        assertEquals(joeysBalance + 1000, joeysNewBalance, 0.001);
        assertEquals(chandlersLimit - 1000, chandlersNewLimit, 0.001);
    }

}