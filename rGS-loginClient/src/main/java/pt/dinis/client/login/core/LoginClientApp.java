package pt.dinis.client.login.core;

import org.apache.log4j.Logger;
import pt.dinis.common.core.Configurations;

import java.io.IOException;

/**
 * Created by tiago on 22-01-2017.
 */
public class LoginClientApp {

    private final static Logger logger = Logger.getLogger(LoginClientApp.class);

    public static void main(String[] args) {

        try {
            Configurations.setPropertiesFromFile("user.properties.file.name", true);
        } catch (IOException e) {
            return;
        }

        LoginClient client = new LoginClient();
        logger.info("Starting login client.");
        client.start();
    }
}
