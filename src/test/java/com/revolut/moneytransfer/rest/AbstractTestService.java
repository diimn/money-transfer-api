package com.revolut.moneytransfer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * The type Abstract test service.
 */
public abstract class AbstractTestService {

    /**
     * The Server.
     */
    static Server server = null;
    /**
     * The Client.
     */
    static HttpClient client;

    /**
     * The Mapper.
     */
    ObjectMapper mapper = new ObjectMapper();

    /**
     * The Builder.
     */
    URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:7777");

    /**
     * Init.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void init() throws Exception {
        startServer();
        HttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        client = HttpClients.custom().setConnectionManager(poolingConnManager).build();


    }

    /**
     * Close client.
     */
    @AfterClass
    public static void closeClient() {
        HttpClientUtils.closeQuietly(client);
    }

    private static void startServer() throws Exception {
        if (server == null || !server.isStarted()) {
            server = new Server(7777);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitOrder(0);
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    ClientService.class.getCanonicalName() + ", "
                    + AccountService.class.getCanonicalName() + ", "
                            + AccountOperationService.class.getCanonicalName());
            server.start();
        }
    }
}
