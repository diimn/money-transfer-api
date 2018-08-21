package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.dao.GenericDAO;
import com.revolut.moneytransfer.dao.db.ClientDAO;
import com.revolut.moneytransfer.model.Client;
import com.revolut.moneytransfer.service.impl.ClientManager;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;

import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Client service.
 */
@Path("/client")
@Produces(MediaType.APPLICATION_JSON)
public class ClientService {

    /**
     * Gets all clients.
     *
     * @return the all clients
     */
    @GET
    @Path("/all")
    public List<Client> getAllClients() {
        ClientManager clientManager = new ClientManager();
        return clientManager.getAllClients();
    }

    /**
     * Gets actual clients.
     *
     * @return the actual clients
     */
    @GET
    @Path("/allActual")
    public List<Client> getActualClients() {
        ClientManager clientManager = new ClientManager();
        return clientManager.getActualClients();
    }

    /**
     * Gets client by id.
     *
     * @param id the id
     * @return the client by id
     */
    @GET
    @Path("id/{id}")
    public Client getClientById(@PathParam("id") long id) {
        ClientManager clientManager = new ClientManager();
        Client client = clientManager.getClientById(id);
        if (client == null) {
            throw new WebApplicationException("Client Not Found", Response.Status.NOT_FOUND);
        }
        return client;
    }

    /**
     * Gets client by info.
     *
     * @param info the info
     * @return the client by info
     */
    @GET
    @Path("info/{info}")
    public Client getClientByInfo(@PathParam("info") String info) {
        ClientManager clientManager = new ClientManager();
        Client client = clientManager.getClientByInfo(info);
        if (client == null) {
            throw new WebApplicationException("Client Not Found", Response.Status.NOT_FOUND);
        }
        return client;
    }

    /**
     * Create client long.
     *
     * @param client the client
     * @return the long
     */
    @POST
    @Path("/create")
    public long createClient(Client client) {
        ClientManager clientManager = new ClientManager();
        Client client1 = clientManager.getClientByInfo(client.getInfo());
        if (client1 == null) {
            return clientManager.createClient(client.getName(), client.getInfo());
        } else {
            throw new WebApplicationException("Client already exist", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Change name response.
     *
     * @param clientId the client id
     * @param newName  the new name
     * @return the response
     */
    @PUT
    @Path("/{clientId}/changeName/{newName}")
    public Response changeName(@PathParam("clientId") long clientId, @PathParam("newName") String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new WebApplicationException("Client name is empty", Response.Status.BAD_REQUEST);
        }
        try {
            ClientManager clientManager = new ClientManager();
            clientManager.updateClientName(clientId, newName);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Change info response.
     *
     * @param clientId the client id
     * @param newInfo  the new info
     * @return the response
     */
    @PUT
    @Path("/{clientId}/changeInfo/{newInfo}")
    public Response changeInfo(@PathParam("clientId") long clientId, @PathParam("newInfo") String newInfo) {
        if (newInfo == null || newInfo.trim().isEmpty()) {
            throw new WebApplicationException("Client name is empty", Response.Status.BAD_REQUEST);
        }
        try {
            ClientManager clientManager = new ClientManager();
            clientManager.updateClientInfo(clientId, newInfo);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Delete client response.
     *
     * @param clientId the client id
     * @return the response
     */
    @DELETE
    @Path("/{clientId}")
    public Response deleteClient(@PathParam("clientId") long clientId) {
        ClientManager clientManager = new ClientManager();
        try {
            clientManager.deleteClient(clientId);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }

}
