package com.revolut.moneytransfer.db;

import com.revolut.moneytransfer.dao.db.ClientDAO;
import com.revolut.moneytransfer.model.Client;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * The type Test client dao.
 */
public class TestClientDAO extends AbstractDAOTest {

    @Test
    public void testFindAll() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            List<Client> clients = clientDAO.find();
            clients.forEach(e -> System.out.println(e.getName()));
            assertTrue(clients.size() > 1);
        }
    }

    @Test
    public void testFindById() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.find(11111111111111113L);
            System.out.println(client.toString());
            assertEquals("Third", client.getName());
        }
    }

    /**
     * Test find by info.
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void testFindByInfo() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.findByInfo("Info3");
            System.out.println(client.toString());
            assertEquals("Third", client.getName());
        }
    }


    @Test
    public void testFindNullById() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = clientDAO.find(-1L);
            System.out.println(client);
            assertNull(client);
        }
    }

    @Test
    public void testCreate() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = new Client("testName", "testInfo");
            long testId = clientDAO.create(client);
            System.out.println(testId);
            Client testCliet = clientDAO.find(testId);
            assertEquals("testName", testCliet.getName());
            assertEquals("testInfo", testCliet.getInfo());
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = new Client("testName1", "testInfo1");
            long testId = clientDAO.create(client);
            Client testClient = clientDAO.find(testId);
            testClient.setName("testName2");
            clientDAO.update(testClient);
            Client updatedClient = clientDAO.find(testId);
            assertEquals("testName2", updatedClient.getName());
        }
    }

    @Test
    public void testDelete() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            ClientDAO clientDAO = new ClientDAO(connection);
            Client client = new Client("testName3", "testInfo3");
            long testId = clientDAO.create(client);
            Client testClient = clientDAO.find(testId);
            assertNotNull(testClient);
            clientDAO.delete(testClient);
            assertTrue(clientDAO.find(testId).isDeleted());
        }
    }
}
