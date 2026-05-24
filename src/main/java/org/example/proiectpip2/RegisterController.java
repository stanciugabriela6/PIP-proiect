package org.example.proiectpip2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Complete all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Passwords do not match");
            return;
        }

        if (password.length() < 8) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Password must be at least 8 characters");
            return;
        }

        if (!email.endsWith("@student.tuiasi.ro")) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email must end with @student.tuiasi.ro");
            return;
        }

        User user = new User(username, email, password, role);
        boolean inserted = UserService.addUser(user);

        if (!inserted) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Username already exists");
            return;
        }

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Registration successful!");

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
        stage.setTitle("SmartDocs - Sign In");
    }

    @FXML
    private void handleGoToRegisterId() {
        String email = emailField.getText().trim();
        if (!email.endsWith("@student.tuiasi.ro")) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email must end with @student.tuiasi.ro");
            return;
        }

        if (email.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Introdu email-ul mai intai!");
            return;
        }

        try {
            FaceApiClient apiClient = new FaceApiClient();
            String result = apiClient.register(email).trim();

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
