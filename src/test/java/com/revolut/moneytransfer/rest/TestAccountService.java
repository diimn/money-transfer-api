package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.model.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * The type Test account service.
 */
public class TestAccountService extends AbstractTestService {


    /**
     * Test get all accounts.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testGetAllAccounts() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String respStr = EntityUtils.toString(response.getEntity());
        Account[] accounts = mapper.readValue(respStr, Account[].class);
        Assert.assertTrue(accounts.length > 0);
    }

    /**
     * Test get acc by id.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testGetAccById() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/id/21111111111111112").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String respStr = EntityUtils.toString(response.getEntity());
        Account account = mapper.readValue(respStr, Account.class);
        Assert.assertEquals(11111111111111111L, account.getClientId());
    }


    /**
     * Test create account.
     *
     * @throws URISyntaxException the uri syntax exception
     * @throws IOException        the io exception
     */
    @Test
    public void testCreateAccount() throws URISyntaxException, IOException {
        Account account = new Account(BigDecimal.valueOf(100), 11111111111111111L);
        HttpResponse response = createAcc(account);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        System.out.println(jsonString);
    }

    /**
     * Test delete account.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testDeleteAccount() throws IOException, URISyntaxException {
        Account account = new Account(BigDecimal.valueOf(100), 11111111111111111L);
        HttpResponse response = createAcc(account);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        URI uri = builder.setPath("/account/" + jsonString).build();

        HttpDelete httpDelete = new HttpDelete(uri);
        httpDelete.setHeader("Content-type", "application/json");
        HttpResponse responseDelete = client.execute(httpDelete);
        int statusDeleteCode = responseDelete.getStatusLine().getStatusCode();
        assertEquals(200, statusDeleteCode);
    }

    /**
     * Test delete not exists account.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testDeleteNotExistsAccount() throws IOException, URISyntaxException {

        String testAcc = "xxx";
        URI uri = builder.setPath("/account/" + testAcc).build();

        HttpDelete httpDelete = new HttpDelete(uri);
        httpDelete.setHeader("Content-type", "application/json");
        HttpResponse responseDelete = client.execute(httpDelete);
        int statusDeleteCode = responseDelete.getStatusLine().getStatusCode();
        assertEquals(404, statusDeleteCode);
    }

    private HttpResponse createAcc(Account account) throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/create").build();
        String jsonInString = mapper.writeValueAsString(account);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        return client.execute(request);
    }

}
