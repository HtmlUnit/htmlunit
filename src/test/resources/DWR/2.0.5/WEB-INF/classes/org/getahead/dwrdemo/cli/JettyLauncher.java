package org.getahead.dwrdemo.cli;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Launch Jetty embedded.
 */
public class JettyLauncher
{
    /**
     * Sets up and runs server.
     * @param args The command line arguments
     * @throws Exception Don't care because top level
     */
    public static void main(String[] args) throws Exception
    {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);
        server.setStopAtShutdown(true);

        server.addHandler(new WebAppContext("web","/dwr"));

        server.start();
        server.join();
    }
}
