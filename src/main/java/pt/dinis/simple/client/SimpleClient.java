package pt.dinis.simple.client;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import pt.dinis.main.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by tiago on 22-01-2017.
 */
public class SimpleClient {

    final static Logger logger = Logger.getLogger(SimpleClient.class);

    private static String server = "localhost";
    private static int port = 1500;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static boolean running;
    private static DateTime time;

    public SimpleClient(String server, int port) {
        this();
        this.server = server;
        this.port = port;
    }

    public SimpleClient() {
        time = new DateTime();
    }

    public boolean start() {
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
            logger.error("Could not open a socket", e);
            Display.alert("Could not open a socket");
            return false;
        }

        Display.info("Socket created.");

        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.error("Problem opening streams for server.", e);
            return false;
        }

        run();

        return true;
    }

    public void run() {
        running = true;

        SimpleClientScanner scanner = new SimpleClientScanner();
        scanner.run();

        while(running) {
            try {
                String message = in.readLine();
                logger.debug("Receiving a message");
                Display.display(message);
            } catch (IOException e) {
                logger.warn("Problem receiving message", e);
            }
        }
    }

    public static boolean close() {
        logger.info("Closing communication with server opened at " + time.toString());

        boolean result = true;
        running = false;

        try {
            in.close();
        } catch (IOException e) {
            logger.info("Problem closing socket input.");
            result = false;
        }

        out.close();

        try {
            socket.close();
        } catch (IOException e) {
            logger.info("Problem closing socket.");
            result = false;
        }

        return result;
    }

    public static boolean sendMessage(String message) {
        if (!socket.isConnected()) {
            close();
            return false;
        }

        out.println(message);
        return true;
    }
}
