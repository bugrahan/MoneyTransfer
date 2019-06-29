package com.revolut.bugrahan.dbReplicas;

import com.revolut.bugrahan.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class DatabaseReplica {
    public DatabaseReplica() {
        // Left blank on purpose.
    }

    private static HashMap<Long, User> userHashMap = new HashMap<>();
    private static HashMap<Long, Account> accountHashMap = new HashMap<>();
    private static HashMap<Long, Transaction> transactionHashMap = new HashMap<>();

    public static HashMap<Long, User> getUserHashMap() {
        return userHashMap;
    }

    public static HashMap<Long, Account> getAccountHashMap() {
        return accountHashMap;
    }

    public static HashMap<Long, Transaction> getTransactionHashMap() {
        return transactionHashMap;
    }

    static {
        User u1 = new User(1000L, "Bugrahan Memis", UserType.PREMIUM);
        User u2 = new User(1001L, "Luke Skywalker", UserType.PREMIUM);
        User u3 = new User(1002L, "Sherlock Holmes", UserType.STANDARD);
        userHashMap.put(u1.getId(), u1);
        userHashMap.put(u2.getId(), u2);
        userHashMap.put(u3.getId(), u3);

        Account a1 = new Account(9001L, 1289.02, Currency.EUR, u1.getId());
        Account a2 = new Account(9002L, 5000.02, Currency.TRY, u1.getId());
        ArrayList arrayList = new ArrayList();
        arrayList.add(a1.getId());
        arrayList.add(a2.getId());
        u1.setAccountIdList(arrayList);
        accountHashMap.put(a1.getId(), a1);
        accountHashMap.put(a2.getId(), a2);

        Account a3 = new Account(9011L, 2989.00, Currency.GBP, u1.getId());
        Account a4 = new Account(9012L, 554.67, Currency.TRY, u1.getId());
        Account a5 = new Account(9013L, 15030.54, Currency.EUR, u1.getId());
        //u2.setAccountIdList(Arrays.asList(a3.getId(), a4.getId(), a5.getId()));

        Account a6 = new Account(9021L, 9000.02, Currency.GBP, u1.getId());
        //u3.setAccountIdList(Arrays.asList(a6.getId()));

        Transaction t1 = new Transaction(transactionHashMap.size()+1, 333, 444, 21, "GBP");
        transactionHashMap.put(t1.getId(), t1);
    }

}
