package com.revolut.moneytransfer.service.impl;

import com.revolut.moneytransfer.dao.db.AccountDAO;
import com.revolut.moneytransfer.dao.db.ClientDAO;
import com.revolut.moneytransfer.ex.DAOException;
import com.revolut.moneytransfer.model.Client;

import com.revolut.moneytransfer.service.IClientManager;
import com.revolut.moneytransfer.util.Utils;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;
import com.revolut.moneytransfer.util.db.LockByIdMap;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * The type Client manager.
 */
public class ClientManager implements IClientManager {

    private final DataSource connectionPool;

    private static final LockByIdMap LOCK_BY_ID_MAP = new LockByIdMap();

    /**
     * Instantiates a new Client manager.
     */
    public ClientManager() {
        connectionPool = H2ConnectionPool.INSTANCE.getConnectionPool();
    }


    @Override
    public Client getClientById(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.find(id);
            if (!client.isDeleted()) {
                return client;
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("Can't get client by Id", e);
        }
    }

    @Override
    public Client getClientByInfo(String info) {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.findByInfo(info);
            if (Utils.checkDeletable(client)) {
                return client;
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("Can't get client by Id", e);
        }
    }


    @Override
    public Client getClientByIdForced(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            return clientDAO.find(id);
        } catch (SQLException e) {
            throw new DAOException("Can't get client by Id", e);
        }
    }

    @Override
    public List<Client> getAllClients() {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            return clientDAO.find();
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }
    }

    @Override
    public List<Client> getActualClients() {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            return clientDAO.find().stream().filter(client -> !client.isDeleted()).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DAOException("Can't get acc by Id", e);
        }
    }

    @Override
    public long createClient(String name, String info) {
        try (Connection connection = connectionPool.getConnection()) {
            Client client = new Client(name, info);
            ClientDAO clientDAO = new ClientDAO(connection);
            return clientDAO.create(client);
        } catch (SQLException e) {
            throw new DAOException("Can't create client", e);
        }
    }

    @Override
    public void updateClientInfo(long id, String info) {
        Lock lockById = LOCK_BY_ID_MAP.get(id).writeLock();
        lockById.lock();
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.find(id);
            if (Utils.checkDeletable(client)) {
                client.setInfo(info);
                clientDAO.update(client);
            } else {
                throw new DAOException("Client doesn't exists or deleted");
            }
        } catch (Exception e) {
            throw new DAOException("Can't update client info", e);
        } finally {
            lockById.unlock();
        }
    }

    @Override
    public void updateClientName(long id, String name) {
        Lock lockById = LOCK_BY_ID_MAP.get(id).writeLock();
        lockById.lock();
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.find(id);
            if (Utils.checkDeletable(client)) {
                client.setName(name);
                clientDAO.update(client);
            } else {
                throw new DAOException("Client doesn't exists or deleted");
            }
        } catch (Exception e) {
            throw new DAOException("Can't update client name", e);
        } finally {
            lockById.unlock();
        }


    }

    @Override
    public void deleteClient(long id) {
        Lock lockById = LOCK_BY_ID_MAP.get(id).writeLock();
        lockById.lock();
        try (Connection connection = connectionPool.getConnection()) {
            connection.setAutoCommit(false);
            try {
                ClientDAO clientDAO = new ClientDAO(connection);
                Client client = clientDAO.find(id);
                if (Utils.checkDeletable(client)) {
                    clientDAO.delete(client);
                    AccountDAO accountDAO = new AccountDAO(connection);
                    /*@todo прикрутить theadsafe*/
                    accountDAO.find()
                            .stream()
                            .filter(account -> account.getClientId() == id)
                            .forEach(accountDAO::delete);
                } else {
                    throw new DAOException("Client not exists or already deleted");
                }
            } catch (Exception e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new DAOException("Can't delete client", e);
        } finally {
            lockById.unlock();
        }

    }
}
