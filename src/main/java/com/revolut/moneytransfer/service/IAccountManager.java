package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.model.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Account manager.
 */
public interface IAccountManager {

    /**
     * Gets acc by id.
     *
     * @param id the id
     * @return the acc by id
     */
    Account getAccById(long id);

    /**
     * Gets all accounts.
     *
     * @return the all accounts
     */
    List<Account> getAllAccounts();

    /**
     * Gets actual accounts.
     *
     * @return the actual accounts
     */
    List<Account> getActualAccounts();

    /**
     * Gets all client accounts.
     *
     * @param clientId the client id
     * @return the all client accounts
     */
    List<Account> getAllClientAccounts(long clientId);

    /**
     * Create account long.
     *
     * @param clientId   the client id
     * @param newBalance the new balance
     * @return the long
     */
    long createAccount(long clientId, BigDecimal newBalance);

    /**
     * Change balance.
     *
     * @param id         the id
     * @param newBalance the new balance
     */
    void changeBalance(long id, BigDecimal newBalance);

    /**
     * Delete account.
     *
     * @param id the id
     */
    void deleteAccount(long id);

}
