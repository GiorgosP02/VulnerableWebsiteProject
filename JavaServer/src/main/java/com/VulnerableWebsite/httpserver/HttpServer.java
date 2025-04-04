package com.VulnerableWebsite.httpserver;

import com.VulnerableWebsite.httpserver.config.Configuration;
import com.VulnerableWebsite.httpserver.config.ConfigurationManager;
import com.VulnerableWebsite.httpserver.core.DatabaseConnection;
import com.VulnerableWebsite.httpserver.core.ServerListenerThread;
import com.VulnerableWebsite.httpserver.core.io.WebRootNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;

/**
 *
 * Driver Class for the Http Server
 *
 */
// I just changed something (testing commit functionality)
public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {

        if (args.length != 1) {
            LOGGER.error("No configuration file provided.");
            LOGGER.error("Syntax:  java -jar simplehttpserver-1.0-SNAPSHOT.jar  <config.json>");
            return;
        }

        LOGGER.info("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile(args[0]);
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using Port: " + conf.getPort());
        LOGGER.info("Using WebRoot: " + conf.getWebroot());

        LOGGER.info("Connecting to the database...");
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Database connected successfully!");
        } else {
            System.out.println("Failed to connect to the database.");
        }

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle later.
        } catch (WebRootNotFoundException e) {
            LOGGER.error("Webroot folder not found",e);
        }


    }

}