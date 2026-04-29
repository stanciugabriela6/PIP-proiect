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

import javafx.event.ActionEvent;
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
        if (role == null)
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please select a role");
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
    private void handleGoToLogin(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent content = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        ((Node) event.getSource()).getScene().setRoot(HelloApplication.buildAnimatedRoot(content));
        stage.setTitle("SmartDocs — Sign In");
    }
    @FXML
    private void handleGoToRegisterId() {
        String email = emailField.getText().trim();
        if(!email.endsWith("@student.tuiasi.ro"))
        {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email must be an email address");
            return;
        }
        System.out.println("Am apasat pe register ID");
        System.out.println("Email trimis: " + email);

        if (email.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Introdu email-ul mai intai!");
            return;
        }

        try {
            FaceApiClient apiClient = new FaceApiClient();
            String result = apiClient.register(email).trim();

            System.out.println("Rezultat API: [" + result + "]");

            if ("REGISTER_FAILED".equals(result)) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Registration failed!");
            } else {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Registration successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Cannot connect to Face ID API!");
        }
    }
}
