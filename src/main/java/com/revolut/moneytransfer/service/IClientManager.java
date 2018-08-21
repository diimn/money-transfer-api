package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.Client;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Client manager.
 */
public interface IClientManager {

    /**
     * Gets client by id.
     *
     * @param id the id
     * @return the client by id
     */
    Client getClientById(long id);

    /**
     * Gets client by id forced.
     *
     * @param id the id
     * @return the client by id forced
     */
    Client getClientByIdForced(long id);

    /**
     * Gets client by info.
     *
     * @param info the info
     * @return the client by info
     */
    Client getClientByInfo(String info);

    /**
     * Gets all clients.
     *
     * @return the all clients
     */
    List<Client> getAllClients();

    /**
     * Gets actual clients.
     *
     * @return the actual clients
     */
    List<Client> getActualClients();

    /**
     * Create client long.
     *
     * @param name the name
     * @param info the info
     * @return the long
     */
    long createClient(String name, String info);

    /**
     * Update client info.
     *
     * @param id   the id
     * @param info the info
     */
    void updateClientInfo(long id, String info);

    /**
     * Update client name.
     *
     * @param id   the id
     * @param name the name
     */
    void updateClientName(long id, String name);

    /**
     * Delete client.
     *
     * @param id the id
     */
    void deleteClient(long id);

}
