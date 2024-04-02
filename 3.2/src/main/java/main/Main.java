package main;

import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import models.UserProfile;
import services.AccountService;
import services.DBService;
import servlets.SignUpServlet;
import servlets.SignInServlet;

public class Main {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getGlobal();
		
		DBService dbService = new DBService();
        dbService.create();
        dbService.check();
		
		AccountService accountService = new AccountService(dbService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");

        Server server = new Server(SERVER_PORT);
        server.setHandler(context);

        server.start();
        logger.info("Server started");
        server.join();
    }
}