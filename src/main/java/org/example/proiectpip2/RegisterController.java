package org.example.proiectpip2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private Label messageLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("JUNIOR", "SENIOR");
    }
    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        if (role == null)
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please select a role");
            return;
        }
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role == null)
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Complete all fields!");
        }
        if(!password.equals(confirmPassword))
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Passwords do not match");
            return;
        }
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Registration Successful");

        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Role: " + role);

        User user = new User(username,email,password,role);
        UserService.addUser(user);

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Registration Successful");

        if(password.length() < 8)
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Password must be at least 8 characters");
        }
        if(!email.endsWith("@student.tuiasi.ro"))
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email must be an email address");
        }

        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        roleComboBox.setValue(null);

    }
    @FXML
    public void handleGoToLogin(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    }

}
