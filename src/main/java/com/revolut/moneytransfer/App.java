package com.revolut.moneytransfer;

import com.revolut.moneytransfer.rest.AccountService;
import com.revolut.moneytransfer.rest.AccountOperationService;
import com.revolut.moneytransfer.rest.ClientService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * The type App.
 */
public class App {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception{
        startServer();
    }

    private static void startServer() throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                ClientService.class.getCanonicalName() + ","
                        + AccountService.class.getCanonicalName() + ","
                        + AccountOperationService.class.getCanonicalName());

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
