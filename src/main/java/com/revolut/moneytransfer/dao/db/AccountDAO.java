package com.revolut.moneytransfer.dao.db;

import com.revolut.moneytransfer.dao.GenericDAO;
import com.revolut.moneytransfer.ex.DAOException;
import com.revolut.moneytransfer.model.Account;

import com.revolut.moneytransfer.util.db.JDBCSupport;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Account dao.
 */
public class AccountDAO extends JDBCSupport implements GenericDAO<Account> {

    /**
     * Instantiates a new Account dao.
     *
     * @param connection the connection
     */
    public AccountDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Account find(long id) {
        Account account = null;
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM Account WHERE AccountId = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("isDeleted")) {
                    account = new Account(rs.getLong("accountId"), rs.getBigDecimal("amount"),
                            rs.getLong("clientId"), rs.getBoolean("isDeleted"));
                    rs.close();
                }
            }
        } catch (Exception e) {
            throw new DAOException("Can't get client whith id: " + id, e);
        }
        return account;
    }

    @Override
    public List<Account> find() {
        List<Account> accounts = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM Account")) {
                while (rs.next()) {
                    Account account = new Account(rs.getLong("accountId"), rs.getBigDecimal("amount"),
                            rs.getLong("clientId"), rs.getBoolean("isDeleted"));
                    accounts.add(account);
                }
            }
            return accounts;
        } catch (Exception e) {
            throw new DAOException("Can't get list of accounts", e);
        }
    }

    @Override
    public long create(Account value) {
        if (value == null) {
            throw new DAOException("Cat't create NULL account");
        }

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO Account (amount, clientId) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setBigDecimal(1, value.getAmount());
            preparedStatement.setLong(2, value.getClientId());
            int insertedRows = preparedStatement.executeUpdate();
            if (insertedRows > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    resultSet.close();
                    return id;
                }
            }
            throw new DAOException("Failed to create acc: " + value);
        } catch (Exception e) {
            throw new DAOException("Failed to create acc: " + value, e);
        }
    }

    @Override
    public void update(Account value) {
        if (value == null) {
            throw new DAOException("Cat't update NULL acc");
        }
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE Account SET amount = ? WHERE accountId = ?")) {
            preparedStatement.setBigDecimal(1, value.getAmount());
            preparedStatement.setLong(2, value.getAccountId());
            int updatedRows = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Failed to update acc: " + value, e);
        }
    }

    @Override
    public void delete(Account value) {
        if (value == null) {
            throw new DAOException("Cat't delete NULL account");
        }
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE  Account SET isDeleted = TRUE WHERE accountId = ?")) {
            preparedStatement.setLong(1, value.getAccountId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Failed to delete acc: " + value, e);
        }
    }
}
