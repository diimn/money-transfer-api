package com.revolut.moneytransfer.db;

import com.revolut.moneytransfer.dao.db.AccountDAO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * The type Test account dao.
 */
public class TestAccountDAO extends AbstractDAOTest {

    @Test
    public void testFindAll() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            List<Account> accounts = accountDAO.find();
            accounts.forEach(e -> System.out.println(e.getAccountId()));
            assertTrue(accounts.size() > 1);
        }
    }

    @Test
    public void testFindById() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account account = accountDAO.find(21111111111111116L);
            System.out.println(account.toString());
            assertEquals(0, account.getAmount().compareTo(BigDecimal.valueOf(600)));
        }
    }

    @Test
    public void testFindNullById() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account client = accountDAO.find(-1L);
            assertNull(client);
        }
    }

    @Test
    public void testCreate() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account account = new Account(BigDecimal.valueOf(500), 11111111111111113L);
            long testId = accountDAO.create(account);
            System.out.println(testId);
            Account testAccount = accountDAO.find(testId);
            assertEquals(0, testAccount.getAmount().compareTo(BigDecimal.valueOf(500)));
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account account = new Account(BigDecimal.valueOf(500), 11111111111111113L);
            long testId = accountDAO.create(account);
            System.out.println(testId);
            Account testAccount = accountDAO.find(testId);
            testAccount.setAmount(BigDecimal.valueOf(1000));
            accountDAO.update(testAccount);
            Account updatedAccount = accountDAO.find(testId);
            assertEquals(0, testAccount.getAmount().compareTo(BigDecimal.valueOf(1000)));
        }
    }

    @Test
    public void testDelete() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account account = new Account(BigDecimal.valueOf(500), 11111111111111113L);
            long testId = accountDAO.create(account);
            System.out.println(testId);
            Account testAccount = accountDAO.find(testId);
            assertNotNull(testAccount);
            accountDAO.delete(testAccount);
            assertNull(accountDAO.find(testId));
        }
    }
}