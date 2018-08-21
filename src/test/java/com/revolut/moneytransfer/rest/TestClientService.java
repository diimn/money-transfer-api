package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.model.Client;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import static org.junit.Assert.assertEquals;

/**
 * The type Test client service.
 */
public class TestClientService extends AbstractTestService {
    /**
     * Test get all clients.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testGetAllClients() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/client/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String respStr = EntityUtils.toString(response.getEntity());
        Client[] clients = mapper.readValue(respStr, Client[].class);
        Assert.assertTrue(clients.length > 0);
    }

    /**
     * Test get client by id.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testGetClientById() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/client/id/11111111111111111").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String respStr = EntityUtils.toString(response.getEntity());
        Client client = mapper.readValue(respStr, Client.class);
        Assert.assertEquals(11111111111111111L, client.getClientId());
    }

    /**
     * Test find client by info.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testFindClientByInfo() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/client/info/Info1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String respStr = EntityUtils.toString(response.getEntity());
        Client client = mapper.readValue(respStr, Client.class);
        Assert.assertEquals("First", client.getName());
    }





    /**
     * Create existing client.
     *
     * @throws URISyntaxException the uri syntax exception
     * @throws IOException        the io exception
     */
    @Test
    public void createExistingClient() throws URISyntaxException, IOException {
        Client client = new Client("test", "Info1");
        HttpResponse response = createClient(client);
        Assert.assertEquals(400, response.getStatusLine().getStatusCode());

    }


    private HttpResponse createClient(Client testClient) throws URISyntaxException, IOException {
        URI uri = builder.setPath("/client/create").build();
        String jsonInString = mapper.writeValueAsString(testClient);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        return response;
    }
}
