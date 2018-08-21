package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.model.AccountOperation;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The type Test account operation service.
 */
public class TestAccountOperationService extends AbstractTestService {


    /**
     * Test operation.
     *
     * @throws IOException        the io exception
     * @throws URISyntaxException the uri syntax exception
     */
    @Test
    public void testOperation() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/operation/transfer").build();
        AccountOperation accountOperation = new AccountOperation(21111111111111111L,
                21111111111111112L, BigDecimal.valueOf(100));

        String jsonString = mapper.writeValueAsString(accountOperation);
        StringEntity entity = new StringEntity(jsonString);
        System.out.println(jsonString);

        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        //check the content
        String respStr = EntityUtils.toString(response.getEntity());
    }

    /**
     * Cycle test.
     *
     * @throws IOException          the io exception
     * @throws URISyntaxException   the uri syntax exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void cycleTest() throws IOException, URISyntaxException, InterruptedException {
        URI uri = builder.setPath("/operation/transfer").build();
        AccountOperation clientOperation = new AccountOperation(21111111111111111L,
                21111111111111112L, BigDecimal.valueOf(1));

        String jsonString = mapper.writeValueAsString(clientOperation);
        StringEntity entity = new StringEntity(jsonString);
        System.out.println(jsonString);

        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);

        final int THREADS_NUM = 100;

        CountDownLatch countDownLatch = new CountDownLatch(THREADS_NUM);

        for (int i = 0; i < THREADS_NUM; i++) {
            new Thread(() -> {
                try {
                    HttpResponse response = client.execute(request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    assertEquals(200, statusCode);
                    String respStr = null;
                    respStr = EntityUtils.toString(response.getEntity());
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.await();

        URI uriGetAll = builder.setPath("/operation/all").build();
        HttpGet requestGet = new HttpGet(uriGetAll);
        HttpResponse response = client.execute(requestGet);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        assertEquals(200, statusCode);
        //check the content
        String respStr = EntityUtils.toString(response.getEntity());
    }
}
