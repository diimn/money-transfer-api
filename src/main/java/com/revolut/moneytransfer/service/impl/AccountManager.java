package com.revolut.moneytransfer.service.impl;

import com.revolut.moneytransfer.dao.db.AccountDAO;
import com.revolut.moneytransfer.ex.DAOException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.service.IAccountManager;
import com.revolut.moneytransfer.util.db.LockByIdMap;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * The type Account manager.
 */
public class AccountManager implements IAccountManager {

    private final DataSource connectionPool;

    private static final LockByIdMap ACCOUNT_LOCK = new LockByIdMap();

    /**
     * Instantiates a new Account manager.
     */
    public AccountManager() {
        connectionPool = H2ConnectionPool.INSTANCE.getConnectionPool();
    }

    @Override
    public Account getAccById(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            return accountDAO.find(id);
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            return accountDAO.find();
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }
    }

    @Override
    public List<Account> getActualAccounts() {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            return accountDAO.find().stream().filter(account -> !account.isDeleted()).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }
    }

    @Override
    public List<Account> getAllClientAccounts(long clientId) {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            return accountDAO.find().stream().filter(account -> account.getClientId() == clientId).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }

    }

    @Override
    public long createAccount(long clientId, BigDecimal balance) {
        if (clientId < 0 && balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DAOException("Can't create new acc");
        }
        Account account = new Account(balance, clientId);
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            long id = accountDAO.create(account);
            return id;
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }
    }

    @Override
    public void changeBalance(long id, BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DAOException("Balance can't be negative");
        }
        Lock lockAccId = ACCOUNT_LOCK.get(id).writeLock();
        lockAccId.lock();
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account account = accountDAO.find(id);
            if (account != null && !account.isDeleted()) {
                account.setAmount(newBalance);
                accountDAO.update(account);
            } else {
                throw new DAOException("Account does not exists or deleted");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update balance for Id: " + id, e);
        } finally {
            lockAccId.unlock();
        }

    }

    @Override
    public void deleteAccount(long id) {
        Lock lockAccId = ACCOUNT_LOCK.get(id).writeLock();
        lockAccId.lock();
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account account = accountDAO.find(id);
            if (account != null && !account.isDeleted()) {
                accountDAO.delete(account);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update balance for Id: " + id, e);
        } finally {
            lockAccId.unlock();
        }

    }
}
