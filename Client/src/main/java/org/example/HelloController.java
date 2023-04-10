package org.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.exampleM.Agent;
import org.exampleR.service.ChatException;
import org.exampleR.service.IChatServices;

import java.io.IOException;
import java.util.Objects;

public class HelloController{
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField agencyTextField;
    @FXML
    private PasswordField passwordTextField;

    private IChatServices server;

    private DestinationController destinationController;

    private Parent root;

    public HelloController(IChatServices server) {
        this.server = server;
        System.out.println("Server for login controller: " + this.server);
    }

    public void setDestinationController(DestinationController destinationController)
    {
        this.destinationController = destinationController;
    }

    public void setChild(Parent root) {
        this.root = root;
        System.out.println("Set root for destination controller");
    }

    public void loginAction(ActionEvent actionEvent) throws IOException, ChatException {
        String username = usernameTextField.getText();
        String agency = agencyTextField.getText();
        String password = passwordTextField.getText();
        if(!Objects.equals(username, "") && !Objects.equals(agency, "") && !Objects.equals(password, ""))
        {
            Agent agent = new Agent(agency,username,password);
            agent.setID(Integer.parseInt(username.substring(username.length() - 1)));

                try
                {
                    server.login(agent,destinationController);

                    Stage stage=new Stage();
                    stage.setTitle("Window for " +agent.getId());
                    stage.setScene(new Scene(root,900,500));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            destinationController.logout();
                            System.exit(0);
                        }
                    });

                    stage.show();

                    destinationController.setAgent(agent);
                    destinationController.init();

                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                }
                catch (ChatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("MPP chat");
                    alert.setHeaderText("Authentication failure");
                    alert.setContentText("Wrong username or password");
                    alert.showAndWait();
                }


            }
        }



}
