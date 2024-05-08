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
import resources.ResourceServerI;
import resources.TestResource;
import services.DBService;
import servlets.ResourcesPageServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;


public class Main {
    private static final int SERVER_PORT = 8080;
    private static final int THREADS_NUMBER = 10;
    static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getGlobal();
        logger.info("Starting at http://127.0.0.1:8080");
        logger.info("Server started");

        AccountServerI accountServer = new AccountServer(10);

        ResourceServerI resourceServer = new TestResource();
        AccountServerControllerMBean serverStatistics = new AccountServerController(accountServer);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("Admin:type=AccountServerController");
        mbs.registerMBean(serverStatistics, name);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ResourcesPageServlet(resourceServer)), ResourcesPageServlet.PAGE_URL);

        DBService dbService = new DBService();
        dbService.create();
        dbService.check();

        context.addServlet(new ServletHolder(new WebSocketChatServlet()), "/chat");


        Server server = new Server(SERVER_PORT);
        server.setHandler(context);

        server.start();
        logger.info("Server started");
        server.join();

        for (int i = 0; i < THREADS_NUMBER; ++i) {
            Thread thread = new RandomSequenceExample();
            System.out.println("Start: " + thread.getName());
            thread.start();
        }
    }
    private static class RandomSequenceExample extends Thread {
        public void run() {
            System.out.println("Run: " + this.getName());
        }
    }
    @SuppressWarnings("UnusedDeclaration")
    private static class SerialSequenceExample extends Thread {
        private static int currentMax = 1;
        private int mainId;
        private final static Object waitObject = new Object();

        public SerialSequenceExample(int id) {
            this.mainId = id;
        }

        public void run() {
            try {
                System.out.println("Run: " + mainId);
                synchronized (waitObject) {
                    while (mainId > currentMax) {
                        waitObject.wait();
                    }

                    currentMax++;
                    waitObject.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
