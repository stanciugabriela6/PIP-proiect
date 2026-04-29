package org.example.proiectpip2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LoginController {

    @FXML
    private Label messageLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // 🔹 LOGIN NORMAL
    @FXML
    public void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Empty fields!");
            return;
        }

        User user = UserService.findUser(username, password);

        if (user != null) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Login successful!");

            openChatBot();

        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Invalid credentials!");
        }
    }

    // 🔹 LOGIN FACE ID
    @FXML
    private void handleGoToFaceId() {
        try {
            FaceApiClient apiClient = new FaceApiClient();
            String result = apiClient.login();

            if ("LOGIN_FAILED".equals(result)) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Face ID failed!");
            } else {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Face ID success!");

                openChatBot();
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("API connection error!");
        }
    }

    // 🔹 NAVIGARE CHATBOT (folosită de ambele)
    private void openChatBot() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.hide();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SmartDocs — ChatBot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new fileUploader.ui.ChatBot());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }

    // 🔹 MERGI LA REGISTER
    @FXML
    private void handleGoToRegister() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();

            Parent content = FXMLLoader.load(getClass().getResource("register-view.fxml"));

            stage.getScene().setRoot(
                    HelloApplication.buildAnimatedRoot(content)
            );

            stage.setTitle("SmartDocs — Create Account");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}