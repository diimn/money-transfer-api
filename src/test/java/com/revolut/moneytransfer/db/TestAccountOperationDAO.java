package com.revolut.moneytransfer.db;

import com.revolut.moneytransfer.dao.db.AccountOperationDAO;
import com.revolut.moneytransfer.model.AccountOperation;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;


/**
 * The type Test account operation dao.
 */
public class TestAccountOperationDAO extends AbstractDAOTest {

    @Override
    public void testFindAll() {
    }

    @Override
    public void testFindById() {
    }

    @Override
    public void testFindNullById() {
    }

    @Test
    public void testCreate() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            AccountOperationDAO accountOperationDAO = new AccountOperationDAO(connection);
            AccountOperation accountOperation = new AccountOperation(21111111111111111L,
                    21111111111111112L, BigDecimal.valueOf(100));
            long testId = accountOperationDAO.create(accountOperation);
            System.out.println(testId);
            AccountOperation testAccountOperation = accountOperationDAO.find(testId);
            System.out.println(testAccountOperation);
            assertTrue(accountOperationDAO.find(testId).getOperationId() > 0);
        }
    }

    @Override
    public void testUpdate() {
    }

    @Override
    public void testDelete() {
    }
}
