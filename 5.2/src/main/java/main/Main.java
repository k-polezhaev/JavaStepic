package main;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import accountServer.AccountServer;
import accountServer.AccountServerController;
import accountServer.AccountServerControllerMBean;
import accountServer.AccountServerI;
import chat.WebSocketChatServlet;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.DBService;

import javax.management.MBeanServer;
import javax.management.ObjectName;


public class Main {
    private static final int SERVER_PORT = 8080;
    static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getGlobal();
        logger.info("Starting at http://127.0.0.1:8080");
        logger.info("Server started");

        AccountServerI accountServer = new AccountServer(10);

        AccountServerControllerMBean serverStatistics = new AccountServerController(accountServer);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("Admin:type=AccountServerController");
        mbs.registerMBean(serverStatistics, name);

        DBService dbService = new DBService();
        dbService.create();
        dbService.check();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new WebSocketChatServlet()), "/chat");


        Server server = new Server(SERVER_PORT);
        server.setHandler(context);

        server.start();
        logger.info("Server started");
        server.join();
    }
}
