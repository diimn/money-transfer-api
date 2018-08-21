package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.model.AccountOperation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface Account operation manager.
 */
public interface IAccountOperationManager {

    /**
     * Transfer money long.
     *
     * @param fromAccId the from acc id
     * @param toAccId   the to acc id
     * @param amount    the amount
     * @return the long
     * @throws SQLException         the sql exception
     * @throws InterruptedException the interrupted exception
     */
    long transferMoney(long fromAccId, long toAccId, BigDecimal amount) throws SQLException, InterruptedException;

    /**
     * Transfer money long.
     *
     * @param accountOperation the account operation
     * @return the long
     * @throws SQLException         the sql exception
     * @throws InterruptedException the interrupted exception
     */
    long transferMoney(AccountOperation accountOperation) throws SQLException, InterruptedException;

    /**
     * Refill money long.
     *
     * @param accId  the acc id
     * @param amount the amount
     * @return the long
     */
    long refillMoney(long accId, BigDecimal amount);

    /**
     * Withdraw money long.
     *
     * @param accId  the acc id
     * @param amount the amount
     * @return the long
     */
    long withdrawMoney(long accId ,BigDecimal amount);

    /**
     * Gets operation by id.
     *
     * @param clientOperationId the client operation id
     * @return the operation by id
     */
    AccountOperation getOperationById(long clientOperationId);

    /**
     * Gets all operations.
     *
     * @return the all operations
     */
    List<AccountOperation> getAllOperations();



}

