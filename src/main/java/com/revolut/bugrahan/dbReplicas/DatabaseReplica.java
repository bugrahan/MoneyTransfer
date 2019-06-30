package com.revolut.bugrahan.dbReplicas;

import com.revolut.bugrahan.model.*;
import com.revolut.bugrahan.model.Currency;

import java.util.*;

public class DatabaseReplica {
    public DatabaseReplica() {
        // Left blank on purpose.
    }

    private static Hashtable<Long, User> userHashtable = new Hashtable<>();
    private static Hashtable<Long, Account> accountHashtable = new Hashtable<>();
    private static Hashtable<Long, Transaction> transactionHashtable = new Hashtable<>();

    public static Hashtable<Long, User> getUserHashtable() {
        return userHashtable;
    }

    public static Hashtable<Long, Account> getAccountHashtable() {
        return accountHashtable;
    }

    public static Hashtable<Long, Transaction> getTransactionHashtable() {
        return transactionHashtable;
    }

    static {
        User u1 = new User(1000L, "Bugrahan Memis", UserType.PREMIUM);
        User u2 = new User(1001L, "Luke Skywalker", UserType.PREMIUM);
        User u3 = new User(1002L, "Sherlock Holmes", UserType.STANDARD);
        userHashtable.put(u1.getId(), u1);
        userHashtable.put(u2.getId(), u2);
        userHashtable.put(u3.getId(), u3);

        Account a1 = new Account(9001L, 1289.02, Currency.EUR, u1.getId());
        Account a2 = new Account(9002L, 5000.02, Currency.TRY, u1.getId());
        ArrayList arrayList = new ArrayList();
        arrayList.add(a1.getId());
        arrayList.add(a2.getId());
        u1.setAccountIdList(arrayList);
        accountHashtable.put(a1.getId(), a1);
        accountHashtable.put(a2.getId(), a2);

        Account a3 = new Account(9011L, 2989.00, Currency.GBP, u1.getId());
        Account a4 = new Account(9012L, 554.67, Currency.TRY, u1.getId());
        Account a5 = new Account(9013L, 15030.54, Currency.EUR, u1.getId());
        arrayList = new ArrayList();
        arrayList.add(a3.getId());
        arrayList.add(a4.getId());
        arrayList.add(a5.getId());
        u2.setAccountIdList(arrayList);
        accountHashtable.put(a3.getId(), a3);
        accountHashtable.put(a4.getId(), a4);
        accountHashtable.put(a5.getId(), a5);

        Account a6 = new Account(9021L, 0, Currency.GBP, u1.getId());
        arrayList = new ArrayList();
        arrayList.add(a6.getId());
        u3.setAccountIdList(arrayList);
        accountHashtable.put(a6.getId(), a6);

        Transaction t1 = new Transaction(transactionHashtable.size()+1, 333, 444, 21, "GBP");
        transactionHashtable.put(t1.getId(), t1);
    }

}
