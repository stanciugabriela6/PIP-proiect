package org.example.proiectpip2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label messageLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    public void handleLogin() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();

        if(username.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Empty fields!");
            return;
        }

        User user = UserService.findUser(username, password);
        if(user != null) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("You are logged in");
        }
        else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("You are not logged in");
        }

    }
    @FXML
    public void handleGoToRegister(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }
}