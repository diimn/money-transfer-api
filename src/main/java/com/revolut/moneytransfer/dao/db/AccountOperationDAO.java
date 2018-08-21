package com.revolut.moneytransfer.dao.db;

import com.revolut.moneytransfer.dao.GenericDAO;
import com.revolut.moneytransfer.ex.DAOException;
import com.revolut.moneytransfer.model.AccountOperation;
import com.revolut.moneytransfer.util.db.JDBCSupport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Account operation dao.
 */
public class AccountOperationDAO extends JDBCSupport implements GenericDAO<AccountOperation> {

    /**
     * Instantiates a new Account operation dao.
     *
     * @param connection the connection
     */
    public AccountOperationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public AccountOperation find(long id) {
        AccountOperation accountOperation = null;
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM ClientOperation WHERE operationId = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                accountOperation = new AccountOperation(
                        rs.getLong("operationId"),
                        rs.getLong("fromAccId"),
                        rs.getLong("toAccId"),
                        rs.getBigDecimal("sum"));
            }
        } catch (Exception e) {
            throw new DAOException("Can't get AccountOperation whith id: " + id, e);
        }
        return accountOperation;
    }

    @Override
    public List<AccountOperation> find() {
        List<AccountOperation> accountOperationList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM ClientOperation")) {
                while (rs.next()) {
                    AccountOperation accountOperation = new AccountOperation(
                            rs.getLong("operationId"),
                            rs.getLong("fromAccId"),
                            rs.getLong("toAccId"),
                            rs.getBigDecimal("sum"));
                    accountOperationList.add(accountOperation);
                }
            }
        } catch (Exception e) {
            throw new DAOException("Can't get ClientOperations");
        }
        return accountOperationList;
    }

    @Override
    public long create(AccountOperation value) {
        if (value == null) {
            throw new DAOException("Cat't create NULL client");
        }
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO ClientOperation (fromAccId, toAccId, sum) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            if (value.getFromAccountId() > 0) {
                preparedStatement.setLong(1, value.getFromAccountId());
            } else {
                preparedStatement.setNull(1, Types.BIGINT);
            }
            if (value.getToAccountId() > 0) {
                preparedStatement.setLong(2, value.getToAccountId());
            } else {
                preparedStatement.setNull(2, Types.BIGINT);
            }

            preparedStatement.setBigDecimal(3, value.getSum());
            int insertedRows = preparedStatement.executeUpdate();
            if (insertedRows > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            throw new DAOException("Failed to create AccountOperation: " + value);
        } catch (Exception e) {
            throw new DAOException("Failed to create AccountOperation: " + value, e);
        }
    }

    @Override
    public void update(AccountOperation value) {
    }

    @Override
    public void delete(AccountOperation value) {
    }


}
