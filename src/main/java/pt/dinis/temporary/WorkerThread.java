package pt.dinis.temporary;

import org.apache.log4j.Logger;
import pt.dinis.common.messages.AuthenticatedMessage;
import pt.dinis.common.messages.GenericMessage;
import pt.dinis.common.messages.basic.BasicMessage;
import pt.dinis.common.messages.basic.CloseConnectionRequest;
import pt.dinis.common.messages.chat.ChatMessage;
import pt.dinis.common.messages.chat.ChatMessageToClient;
import pt.dinis.common.messages.chat.ChatMessageToServer;
import pt.dinis.common.messages.user.*;
import pt.dinis.main.Dealer;
import pt.dinis.common.Display;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by tiago on 22-01-2017.
 */
public class WorkerThread extends Thread {

    private final static Logger logger = Logger.getLogger(WorkerThread.class);

    private GenericMessage message;
    private Integer id;
    private Boolean isAuthenticated;

    public WorkerThread(GenericMessage message, int id) {
        this.message = message;
        this.id = id;
    }

    @Override
    public void run() {
        isAuthenticated = false;
        if (message instanceof  AuthenticatedMessage) {
            AuthenticatedMessage authenticatedMessage = (AuthenticatedMessage) message;
            if (authenticatedMessage.isAuthenticated()) {
                if (Dealer.isAuthenticated(id, authenticatedMessage.getToken())) {
                    isAuthenticated = true;
                }
            }
            message = authenticatedMessage.getMessage();
        }

        try {
            if (message instanceof UserMessage) {
                userProtocol((UserMessage) message);
            } else if (message instanceof ChatMessage) {
                chatProtocol((ChatMessage) message);
            } else if (message instanceof BasicMessage) {
                basicProtocol((BasicMessage) message);
            } else {
                logger.warn("Unexpected message from client " + id + ": " + message);
            }
        } catch (Exception e) {
            Display.alert("error in message '" + message + "' from client " + id);
            logger.error("Error interpreting message '" + message + "'", e);
        }
    }

    private void userProtocol(UserMessage message) {
        if (message instanceof LoginRequest) {
            // TODO add check name and password
            login();
        } else if (message instanceof RegisterRequest) {
            // TODO to implement
            throw new NotImplementedException();
        } else if (message instanceof LogoutRequest) {
            logout();
        } else if (message instanceof ReLoginRequest) {
            ReLoginRequest reloginMessage = (ReLoginRequest) message;
            relogin(reloginMessage.getToken());
        } else {
            logger.warn("Unexpected message from client " + id + ": " + message);
        }
    }

    private void basicProtocol(BasicMessage message) {
        if (message instanceof CloseConnectionRequest) {
            close();
        } else {
            logger.warn("Unexpected message from client " + id + ": " + message);
        }
    }

    private void chatProtocol(ChatMessage message) {
        if (message instanceof ChatMessageToServer) {
            ChatMessageToServer chatMessage = (ChatMessageToServer) message;
            message(chatMessage.getMessage(), chatMessage.getType(),
                    chatMessage.getDestiny(), chatMessage.getPerson());
            return;
        } else {
            logger.warn("Unexpected message from client " + id + ": " + message);
        }
    }

    private void login() {
        Display.info("Log in client " + id);
        Dealer.loginClient(id);
    }

    private void relogin(String token) {
        Display.info("Relog in client " + id + " with token '" + token + "'");
        Dealer.reloginClient(id, token);
    }

    private void logout() {
        Display.info("Log out client " + id);
        Dealer.logoutClient(id);
    }

    private void close() {
        Dealer.disconnectClient(id);
    }

    private void message(String message, ChatMessage.ChatMessageType type,
                         ChatMessageToServer.Destiny destiny, Integer person) {


        if(message.isEmpty()) {
            Display.alert("client " + id + " sent an empty message");
            return;
        }

        Collection<Integer> ids = Collections.emptySet();
        switch(destiny) {
            case ALL:
                ids = Dealer.getActiveClients();
                break;
            case ECHO:
                ids = Collections.singleton(id);
                break;
            case SERVER:
                break;
            case OTHERS:
                ids = Dealer.getActiveClients();
                ids.remove(id);
                break;
            case SPECIFIC:
                ids = Collections.singleton(person);
                break;
        }

        if (destiny == ChatMessageToServer.Destiny.SERVER) {
            switch (type) {
                case NORMAL:
                    Display.display("client " + id + " said: " + message);
                    break;
                case ERROR:
                    Display.alert("client " + id + " said: " + message);
                    break;
            }
            return;
        }

        Dealer.sendMessage(ids, new ChatMessageToClient(message, type));
    }
}
