package com.revolut.moneytransfer.service.impl;

import com.revolut.moneytransfer.dao.db.AccountDAO;
import com.revolut.moneytransfer.dao.db.AccountOperationDAO;
import com.revolut.moneytransfer.ex.DAOException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.AccountOperation;
import com.revolut.moneytransfer.service.IAccountOperationManager;
import com.revolut.moneytransfer.util.Utils;
import com.revolut.moneytransfer.util.db.LockByIdMap;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * The type Account operation manager.
 */
public class AccountOperationManager implements IAccountOperationManager {

    private final DataSource connectionPool;

    private static final LockByIdMap ACCOUNT_LOCK = new LockByIdMap();

    /**
     * Instantiates a new Account operation manager.
     */
    public AccountOperationManager() {
        connectionPool = H2ConnectionPool.INSTANCE.getConnectionPool();
    }

    @Override
    public long transferMoney(long fromAccId, long toAccId, BigDecimal amount) throws SQLException {
        long transactionId = 0;

        //exclude deadlock
        long firstLockAccId;
        long secondLockAccId;


        if (fromAccId < toAccId) {
            firstLockAccId = fromAccId;
            secondLockAccId = toAccId;
        } else {
            firstLockAccId = toAccId;
            secondLockAccId = fromAccId;
        }

        Lock lockFirst = ACCOUNT_LOCK.get(firstLockAccId).writeLock();
        Lock lockSecond = ACCOUNT_LOCK.get(secondLockAccId).writeLock();

        lockFirst.lock();
        try {
            boolean lockedTo;
            try {
                lockedTo = lockSecond.tryLock(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new DAOException("!!!InterruptedException", e);
            }
            if (lockedTo) {
                try (Connection connection = connectionPool.getConnection()) {
                    connection.setAutoCommit(false);
                    try {
                        AccountDAO accountDAO = new AccountDAO(connection);
                        Account fromAcc = accountDAO.find(fromAccId);
                        if (Utils.checkDeletable(fromAcc)) {
                            if (checkBalance(fromAcc, amount)) {
                                Account toAcc = accountDAO.find(toAccId);
                                remittance(fromAcc, toAcc, amount);
                                accountDAO.update(fromAcc);
                                accountDAO.update(toAcc);
                                AccountOperation accountOperation = new AccountOperation(fromAccId, toAccId, amount);
                                AccountOperationDAO accountOperationDAO = new AccountOperationDAO(connection);
                                transactionId = accountOperationDAO.create(accountOperation);
                            } else {
                                throw new DAOException("Not enough money for transfer");
                            }
                        } else {
                            throw new DAOException("Account does not exists: " + fromAccId);
                        }
                    } catch (Exception e) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw e;
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
                    return transactionId;
                } catch (Exception e) {
                    throw new DAOException("Can't transfer", e);
                } finally {
                    lockSecond.unlock();
                }
            } else {
                lockFirst.unlock();
                lockFirst = null;
                // try again
                transferMoney(fromAccId, toAccId, amount);
            }
        } finally {
            if (lockFirst != null) {
                lockFirst.unlock();
            }
        }
        return transactionId;
    }

    @Override
    public long transferMoney(AccountOperation accountOperation) throws SQLException {
        return transferMoney(accountOperation.getFromAccountId(), accountOperation.getToAccountId(), accountOperation.getSum());
    }

    @Override
    public long refillMoney(long accId, BigDecimal amount) {
        Lock lockAcc = ACCOUNT_LOCK.get(accId).writeLock();
        lockAcc.lock();
        try (Connection connection = connectionPool.getConnection()) {
            long transactionId;
            connection.setAutoCommit(false);
            try {
                AccountDAO accountDAO = new AccountDAO(connection);
                Account account = accountDAO.find(accId);
                if (Utils.checkDeletable(account)) {
                    account.setAmount(account.getAmount().add(amount));
                    accountDAO.update(account);
                    AccountOperation accountOperation = new AccountOperation(0, accId, amount);
                    AccountOperationDAO accountOperationDAO = new AccountOperationDAO(connection);
                    transactionId = accountOperationDAO.create(accountOperation);
                } else {
                    throw new DAOException("Account does not exists");
                }
            } catch (Exception e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
            connection.commit();
            connection.setAutoCommit(true);
            return transactionId;
        } catch (Exception e) {
            throw new DAOException("Can't transfer", e);
        } finally {
            lockAcc.unlock();
        }
    }

    @Override
    public long withdrawMoney(long accId, BigDecimal amount) {
        Lock lockAcc = ACCOUNT_LOCK.get(accId).writeLock();
        lockAcc.lock();
        try (Connection connection = connectionPool.getConnection()) {
            long transactionId;
            connection.setAutoCommit(false);
            try {
                AccountDAO accountDAO = new AccountDAO(connection);
                Account account = accountDAO.find(accId);
                if (Utils.checkDeletable(account)) {
                    if (checkBalance(account, amount)) {
                        account.setAmount(account.getAmount().subtract(amount));
                        accountDAO.update(account);
                        AccountOperation accountOperation = new AccountOperation(accId, 0, amount);
                        AccountOperationDAO accountOperationDAO = new AccountOperationDAO(connection);
                        transactionId = accountOperationDAO.create(accountOperation);
                    } else {
                        throw new DAOException("Not enough money");
                    }
                } else {
                    throw new DAOException("Account does not exists");
                }
            } catch (Exception e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
            connection.commit();
            connection.setAutoCommit(true);
            return transactionId;
        } catch (Exception e) {
            throw new DAOException("Can't transfer", e);
        } finally {
            lockAcc.unlock();
        }
    }

    @Override
    public AccountOperation getOperationById(long clientOperationId) {
        try (Connection connection = connectionPool.getConnection()) {
            AccountOperationDAO accountOperationDAO = new AccountOperationDAO(connection);
            return accountOperationDAO.find(clientOperationId);
        } catch (SQLException e) {
            throw new DAOException("Can't get operations by Id", e);
        }
    }

    @Override
    public List<AccountOperation> getAllOperations() {
        try (Connection connection = connectionPool.getConnection()) {
            AccountOperationDAO accountOperationDAO = new AccountOperationDAO(connection);
            return accountOperationDAO.find();
        } catch (SQLException e) {
            throw new DAOException("Can't get operations", e);
        }
    }

    private boolean checkBalance(Account account, BigDecimal sum) {
        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DAOException("Sum must be positive");
        }
        return (account.getAmount().compareTo(sum) >= 0);
    }

    private void remittance(Account from, Account to, BigDecimal sum) {
        from.setAmount(from.getAmount().subtract(sum));
        to.setAmount(to.getAmount().add(sum));
    }
}