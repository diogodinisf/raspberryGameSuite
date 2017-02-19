package pt.dinis.communication;

import pt.dinis.main.Display;
import pt.dinis.temporary.LoginWorkerThread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by diogo on 05-02-2017.
 */
public class ClientCommunicationProtocol {

    public enum MessageType {
        LOGIN("login"),
        LOGOUT("logout"),
        CLOSE("disconnect"),
        MESSAGE("message"),
        ERROR("error");

        private String word;

        MessageType(String word) {
            this.word = word;
        }

        public String getWord() {
            return word;
        }
    }

    public static boolean protocol(String message) {
        List<String> words = splitMessage(message);

        if(words.isEmpty()) {
            return false;
        }

        String word = words.get(0).toLowerCase();

        if(ClientCommunicationProtocol.MessageType.LOGIN.getWord().equals(word)) {
            if (words.size() >= 4) {
                Display.info("Login trial: " + words);
                LoginWorkerThread loginWorkerThread = new LoginWorkerThread(
                        words.get(1), words.get(2), words.get(3));
                loginWorkerThread.start();
                return true;
            }
            Display.alert("Login failed: " + words);
            return false;
        }

        if(ClientCommunicationProtocol.MessageType.CLOSE.getWord().equals(word)) {
            // new workerthread
            return true;
        }

        if(ClientCommunicationProtocol.MessageType.LOGOUT.getWord().equals(word)) {
            // new workerthread
            return true;
        }

        if(ClientCommunicationProtocol.MessageType.ERROR.getWord().equals(word)) {
            // new workerthread
            return true;
        }

        if(ClientCommunicationProtocol.MessageType.MESSAGE.getWord().equals(word)) {
            // new workerthread
            return true;
        }

        return false;
    }

    private static List<String> splitMessage(String message) {
        List<String> words = (new ArrayList<>());
        words.addAll(Arrays.asList(message.split("\\s+")));
        while(words.remove("")) { }
        return words;
    }


}
