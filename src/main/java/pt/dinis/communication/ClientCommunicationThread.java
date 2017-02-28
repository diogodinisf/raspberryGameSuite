package pt.dinis.communication;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import pt.dinis.common.Display;
import pt.dinis.common.messages.GenericMessage;
import pt.dinis.common.messages.MessagesUtils;
import pt.dinis.main.Dealer;
import pt.dinis.temporary.WorkerThread;

import java.io.*;
import java.net.Socket;

/**
 * Created by tiago on 22-01-2017.
 */
public class ClientCommunicationThread extends Thread{

    private final static Logger logger = Logger.getLogger(ClientCommunicationThread.class);

    private Integer id;
    private Socket socket;
    private boolean running;
    private PrintWriter out;
    private BufferedReader in;
    private final DateTime time;

    public ClientCommunicationThread(Socket socket, Integer id) throws IOException {

        this.id = id;
        this.socket = socket;
        this.running = true;
        this.time = new DateTime();

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while(running) {
            try {
                GenericMessage message = MessagesUtils.decode(in.readLine());
                if (message.getDirection() == GenericMessage.Direction.CLIENT_TO_SERVER) {
                    logger.debug("Receiving and sending a message " + message);
                    WorkerThread temporaryThread = new WorkerThread(message, id);
                    temporaryThread.run();
                } else {
                    logger.warn("Server got a message supposedly from server to client: " + message);
                    Display.alert("Wrong message from " + id);
                }
            } catch (ClassCastException e) {
                Display.alert("Received wrong message format");
                logger.error("Received message is not of a correct class: ", e);
            } catch (NullPointerException e) {
                logger.warn("The connection to client " + id + " has been lost.");
                Dealer.disconnectClient(id);
            } catch (IOException e) {
                if(running) {
                    logger.warn("Problem receiving message", e);
                } else if (!toContinue()) {
                    Dealer.disconnectClient(id);
                }
            }
        }
    }

    public boolean close() {
        logger.info("Closing thread of client " + id + " opened at " + time.toString());

        boolean result = true;
        running = false;

        try {
            socket.close();
        } catch (IOException e) {
            logger.info("Problem closing socket of client " + id + ".", e);
            result = false;
        }

        try {
            in.close();
        } catch (IOException e) {
            logger.info("Problem closing socket input of client " + id + ".", e);
            result = false;
        }

        try {
           out.close();
        } catch (NullPointerException e) {
            logger.info("Problem closing socket output of client " + id + ".", e);
            result = false;
        }
        return result;
    }

    private boolean toContinue() {
        if(!socket.isConnected()) {
            return false;
        }

        return true;
    }

    public boolean sendMessage(GenericMessage message) {
        if (!toContinue()) {
            Dealer.disconnectClient(id);
            return false;
        }

        try {
            out.println(MessagesUtils.encode(message));
        } catch (IOException e) {
            logger.warn("Error sending message " + message.toString(), e);
            return false;
        }
        return true;
    }
}
