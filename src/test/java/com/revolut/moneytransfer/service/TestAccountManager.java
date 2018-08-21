package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.service.impl.AccountManager;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * The type Test account manager.
 */
public class TestAccountManager {
    /**
     * The Account manager.
     */
    AccountManager accountManager = new AccountManager();

    /**
     * Test get acc by id.
     */
    @Test
    public void testGetAccById() {
        Account account = accountManager.getAccById(21111111111111111L);
        System.out.println(account);
        TestCase.assertNotNull(account);
    }

    /**
     * Test get all accs.
     */
    @Test
    public void testGetAllAccs() {
        List<Account> accounts = accountManager.getAllAccounts();
        System.out.println(accounts);
        TestCase.assertTrue(accounts.size() > 0);
    }

    /**
     * Test delete account.
     */
    @Test
    public void testDeleteAccount() {
        List<Account> actualAccs = accountManager.getActualAccounts();
        Account account = actualAccs.get(0);
        long accountId = account.getAccountId();
        accountManager.deleteAccount(accountId);
        TestCase.assertNull(accountManager.getAccById(accountId));

    }

    /**
     * Test get all actual accs.
     */
    @Test
    public void testGetAllActualAccs() {
        List<Account> actualAccsBefore = accountManager.getActualAccounts();
        System.out.println(actualAccsBefore);
        accountManager.deleteAccount(actualAccsBefore.get(1).getAccountId());
        List<Account> actualAccssAfter = accountManager.getActualAccounts();
        System.out.println(actualAccssAfter);
        TestCase.assertTrue(actualAccssAfter.size() < actualAccsBefore.size());
    }


}
