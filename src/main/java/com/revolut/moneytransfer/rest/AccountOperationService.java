package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.model.AccountOperation;
import com.revolut.moneytransfer.service.impl.AccountOperationManager;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The type Account operation service.
 */
@Path("/operation")
@Produces(MediaType.APPLICATION_JSON)
public class AccountOperationService {


    /**
     * Gets client operation by id.
     *
     * @param clientOperationId the client operation id
     * @return the client operation by id
     */
    @GET
    @Path("/{clientOperationId}")
    public AccountOperation getClientOperationById(@PathParam("clientOperationId") long clientOperationId) {
        AccountOperationManager accountOperationManager = new AccountOperationManager();
        return accountOperationManager.getOperationById(clientOperationId);
    }

    /**
     * Gets all operations.
     *
     * @return the all operations
     */
    @GET
    @Path("/all")
    public List<AccountOperation> getAllOperations() {
        AccountOperationManager accountOperationManager = new AccountOperationManager();
        return accountOperationManager.getAllOperations();
    }

    /**
     * Transfer response.
     *
     * @param accountOperation the account operation
     * @return the response
     */
    @POST
    @Path("/transfer")
    public Response transfer(AccountOperation accountOperation) {
        AccountOperationManager accountOperationManager = new AccountOperationManager();
        try {
            accountOperationManager.transferMoney(accountOperation);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Refill response.
     *
     * @param accountOperation the account operation
     * @return the response
     */
    @POST
    @Path("/refill")
    public Response refill(AccountOperation accountOperation) {
        AccountOperationManager accountOperationManager = new AccountOperationManager();
        try {
            accountOperationManager.refillMoney(accountOperation.getToAccountId(), accountOperation.getSum());
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Withdraw response.
     *
     * @param accountOperation the account operation
     * @return the response
     */
    @POST
    @Path("/withdraw")
    public Response withdraw(AccountOperation accountOperation) {
        AccountOperationManager accountOperationManager = new AccountOperationManager();
        try {
            accountOperationManager.withdrawMoney(accountOperation.getFromAccountId(), accountOperation.getSum());
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }
}
