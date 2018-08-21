package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.model.Client;
import com.revolut.moneytransfer.service.impl.ClientManager;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * The type Test client manager.
 */
public class TestClientManager {
    /**
     * The Client manager.
     */
    ClientManager clientManager = new ClientManager();

    /**
     * Test get client by id.
     */
    @Test
    public void testGetClientById() {
        Client client = clientManager.getClientById(11111111111111111L);
        System.out.println(client);
        TestCase.assertNotNull(client);
    }

    /**
     * Test get client by info.
     */
    @Test
    public void testGetClientByInfo() {
        System.out.println(clientManager.getActualClients());
        Client client = clientManager.getClientByInfo("Info3");
        System.out.println(client);
        TestCase.assertNotNull(client);
    }

    /**
     * Test get all clients.
     */
    @Test
    public void testGetAllClients() {
        List<Client> clients = clientManager.getAllClients();
        System.out.println(clients);
        TestCase.assertTrue(clients.size() > 0);
    }

    /**
     * Test delete client.
     */
    @Test
    public void testDeleteClient() {
        List<Client> actualClients = clientManager.getActualClients();
        Client targetClient = actualClients.get(0);
        long targetClientId = targetClient.getClientId();
        clientManager.deleteClient(targetClientId);
        System.out.println(clientManager.getClientById(targetClientId));
        TestCase.assertNull(clientManager.getClientById(targetClientId));

    }

    /**
     * Test get all actual clients.
     */
    @Test
    public void testGetAllActualClients() {
        List<Client> actualClientsBefore = clientManager.getActualClients();
        System.out.println(actualClientsBefore);
        clientManager.deleteClient(actualClientsBefore.get(1).getClientId());
        List<Client> actualClientsAfter = clientManager.getActualClients();
        System.out.println(actualClientsBefore);
        TestCase.assertTrue(actualClientsAfter.size() < actualClientsBefore.size());
    }


}
