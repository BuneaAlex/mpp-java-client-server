package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.exampleN.objectprotocol.ChatServicesObjectProxy;
import org.exampleR.service.IChatServices;

import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("In start");

        Properties clientProps = new Properties();
        try {
            clientProps.load(HelloApplication.class.getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IChatServices server = new ChatServicesObjectProxy(serverIP, serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new HelloController(server));
        Parent root = fxmlLoader.load();

        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("destination-view.fxml"));
        fxmlLoader2.setControllerFactory(controllerClass -> new DestinationController(server));
        Parent root2 = fxmlLoader2.load();

        Scene scene = new Scene(root, 900, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        HelloController c = fxmlLoader.getController();
        DestinationController d = fxmlLoader2.getController();
        c.setDestinationController(d);
        c.setChild(root2);
        System.out.println(d);
    }

    public static void main(String[] args) {
        launch();
    }
}
