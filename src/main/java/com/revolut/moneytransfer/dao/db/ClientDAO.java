package com.revolut.moneytransfer.dao.db;

import com.revolut.moneytransfer.dao.GenericDAO;
import com.revolut.moneytransfer.ex.DAOException;
import com.revolut.moneytransfer.model.Client;
import com.revolut.moneytransfer.util.db.JDBCSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Client dao.
 */
public class ClientDAO extends JDBCSupport implements GenericDAO<Client> {

    /**
     * Instantiates a new Client dao.
     *
     * @param connection the connection
     */
    public ClientDAO(Connection connection) {
        super(connection);
    }


    public Client find(long id) {
        Client client = null;
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM Client WHERE clientId = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                client = new Client(rs.getLong("clientId"), rs.getString("name"),
                        rs.getString("info"), rs.getBoolean("isDeleted"));

            }
        } catch (Exception e) {
            throw new DAOException("Can't get client whith id: " + id, e);
        }
        return client;
    }


    public List<Client> find() {
        List<Client> clients = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM Client")) {
                while (rs.next()) {
                    Client client = new Client(rs.getLong("clientId"), rs.getString("name"),
                            rs.getString("info"), rs.getBoolean("isDeleted"));
                    clients.add(client);
                }
            }
            return clients;
        } catch (Exception e) {
            throw new DAOException("Can't get list of clients", e);
        }
    }

    public long create(Client value) {
        if (value == null) {
            throw new DAOException("Cat't create NULL client");
        }
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO Client (name, info) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, value.getName());
            preparedStatement.setString(2, value.getInfo());
            int insertedRows = preparedStatement.executeUpdate();
            if (insertedRows > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            throw new DAOException("Failed to create client: " + value);
        } catch (Exception e) {
            throw new DAOException("Failed to create client: " + value, e);
        }
    }

    public void update(Client value) {
        if (value == null) {
            throw new DAOException("Cat't update NULL client");
        }
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE  Client SET name = ?, info = ? WHERE clientId = ?")) {
            preparedStatement.setString(1, value.getName());
            preparedStatement.setString(2, value.getInfo());
            preparedStatement.setLong(3, value.getClientId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Failed to update client: " + value, e);
        }

    }

    public void delete(Client value) {
        if (value == null) {
            throw new DAOException("Cat't delete NULL client");
        }
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE  Client SET isDeleted = TRUE WHERE clientId = ?")) {
            preparedStatement.setLong(1, value.getClientId());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new DAOException("Failed to delete client: " + value, e);
        }
    }

    /**
     * Find by info client.
     *
     * @param info the info
     * @return the client
     */
    public Client findByInfo(String info) {
        Client client = null;
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM Client WHERE info LIKE ?")) {
            preparedStatement.setString(1, info);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                client = new Client(rs.getLong("clientId"), rs.getString("name"),
                        rs.getString("info"), rs.getBoolean("isDeleted"));
            }
        } catch (Exception e) {
            throw new DAOException("Can't get client whith info: " + info, e);
        }
        return client;
    }
}
