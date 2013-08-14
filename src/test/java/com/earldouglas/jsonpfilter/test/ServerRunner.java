package com.earldouglas.jsonpfilter.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ServerRunner {

    static Server server;
    static final int PORT = 8080;

    @BeforeClass public static void startWebapp() throws Exception {
        server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(PORT);

        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/jsonp-filter");
        webAppContext.setWar("src/test/webapp");

        server.setHandler(webAppContext);

        server.start();
    }

    @AfterClass public static void stopWebapp() throws Exception {
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        startWebapp();
        server.join();
    }
}
