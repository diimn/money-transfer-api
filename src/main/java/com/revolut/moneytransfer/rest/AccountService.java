package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.dao.GenericDAO;
import com.revolut.moneytransfer.dao.db.ClientDAO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.Client;
import com.revolut.moneytransfer.service.impl.AccountManager;
import com.revolut.moneytransfer.service.impl.ClientManager;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;

import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * The type Account service.
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    /**
     * Gets all accounts.
     *
     * @return the all accounts
     */
    @GET
    @Path("/all")
    public List<Account> getAllAccounts() {
        AccountManager accountManager = new AccountManager();
        return accountManager.getAllAccounts();
    }

    /**
     * Gets actual accounts.
     *
     * @return the actual accounts
     */
    @GET
    @Path("/allActual")
    public List<Account> getActualAccounts() {
        AccountManager accountManager = new AccountManager();
        return accountManager.getActualAccounts();
    }

    /**
     * Gets account by id.
     *
     * @param id the id
     * @return the account by id
     */
    @GET
    @Path("id/{id}")
    public Account getAccountById(@PathParam("id") long id) {
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.getAccById(id);
        if (account == null) {
            throw new WebApplicationException("Account Not Found", Response.Status.NOT_FOUND);
        }
        return account;

    }

    /**
     * Gets client accounts.
     *
     * @param clientId the client id
     * @return the client accounts
     */
    @GET
    @Path("/client/{clientId}")
    public List<Account> getClientAccounts(@PathParam("clientId") long clientId) {
        AccountManager accountManager = new AccountManager();
        List<Account> accountList = accountManager.getAllClientAccounts(clientId);
        if (accountList == null) {
            throw new WebApplicationException("Client accs Not Found", Response.Status.NOT_FOUND);
        }
        return accountList;
    }

    /**
     * Create acount long.
     *
     * @param clientId the client id
     * @param amount   the amount
     * @return the long
     */
    @POST
    @Path("/create/{clientId}/amount/{amount}")
    public long createAcount(@PathParam("clientId") long clientId, @PathParam("amount")BigDecimal amount) {
        AccountManager accountManager = new AccountManager();
        try {
            return accountManager.createAccount(clientId, amount);
        } catch (Exception e){
            throw new WebApplicationException("Client already exists", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Create acount long.
     *
     * @param account the account
     * @return the long
     */
    @POST
    @Path("/create/")
    public long createAcount(Account account) {
        AccountManager accountManager = new AccountManager();
        if (accountManager.getAccById(account.getAccountId()) == null) {
            return accountManager.createAccount(account.getClientId(), account.getAmount());
        } else {
            throw new WebApplicationException("Client already exist", Response.Status.BAD_REQUEST);
        }
    }


    /**
     * Delete account response.
     *
     * @param accountId the account id
     * @return the response
     */
    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId) {
        AccountManager accountManager = new AccountManager();
        try {
            accountManager.deleteAccount(accountId);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException("Operation failed", Response.Status.BAD_REQUEST);
        }
    }
}
