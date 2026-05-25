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

/**
 * Controller responsabil pentru
 * înregistrarea utilizatorilor.
 *
 * Clasa gestionează:
 * validarea datelor introduse,
 * crearea conturilor și
 * înregistrarea prin Face ID.
 */
public class RegisterController {

    /**
     * Etichetă utilizată pentru afișarea
     * mesajelor de eroare sau succes.
     */
    @FXML
    private Label messageLabel;

    /**
     * Câmp pentru username.
     */
    @FXML
    private TextField usernameField;

    /**
     * Câmp pentru email.
     */
    @FXML
    private TextField emailField;

    /**
     * Câmp pentru parolă.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Câmp pentru confirmarea parolei.
     */
    @FXML
    private PasswordField confirmPasswordField;

    /**
     * ComboBox pentru selectarea rolului.
     */
    @FXML
    private ComboBox<String> roleComboBox;

    /**
     * Inițializează lista de roluri
     * disponibile pentru utilizator.
     */
    @FXML
    public void initialize() {

        roleComboBox.getItems().addAll(
                "JUNIOR",
                "SENIOR"
        );
    }

    /**
     * Gestionează procesul
     * de înregistrare al utilizatorului.
     */
    @FXML
    public void handleRegister() {

        String username =
                usernameField.getText().trim();

        String email =
                emailField.getText().trim();

        String password =
                passwordField.getText();

        String confirmPassword =
                confirmPasswordField.getText();

        String role =
                roleComboBox.getValue();

        /**
         * Verifică existența
         * câmpurilor goale.
         */
        if (username.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()
                || role == null) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText("Complete all fields!");

            return;
        }

        /**
         * Verifică dacă parolele coincid.
         */
        if (!password.equals(confirmPassword)) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText("Passwords do not match");

            return;
        }

        /**
         * Verifică lungimea parolei.
         */
        if (password.length() < 8) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText(
                    "Password must be at least 8 characters"
            );

            return;
        }

        /**
         * Verifică domeniul email-ului.
         */
        if (!email.endsWith("@student.tuiasi.ro")) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText(
                    "Email must end with @student.tuiasi.ro"
            );

            return;
        }

        User user = new User(
                username,
                email,
                password,
                role
        );

        boolean inserted =
                UserService.addUser(user);

        /**
         * Verifică dacă username-ul există deja.
         */
        if (!inserted) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText("Username already exists");

            return;
        }

        messageLabel.setStyle("-fx-text-fill: green;");

        messageLabel.setText("Registration successful!");

        /**
         * Resetează câmpurile formularului.
         */
        usernameField.clear();

        emailField.clear();

        passwordField.clear();

        confirmPasswordField.clear();

        roleComboBox.setValue(null);
    }

    /**
     * Navighează către pagina de login.
     *
     * @param event evenimentul asociat butonului
     * @throws IOException dacă fișierul FXML
     * nu poate fi încărcat
     */
    @FXML
    private void handleGoToLogin(
            javafx.event.ActionEvent event
    ) throws IOException {

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        Parent content =
                FXMLLoader.load(
                        getClass().getResource("hello-view.fxml")
                );

        ((Node) event.getSource())
                .getScene()
                .setRoot(
                        HelloApplication.buildAnimatedRoot(content)
                );

        stage.setTitle("SmartDocs - Sign In");
    }

    /**
     * Gestionează procesul
     * de înregistrare Face ID.
     */
    @FXML
    private void handleGoToRegisterId() {

        String email =
                emailField.getText().trim();

        /**
         * Verifică domeniul email-ului.
         */
        if (!email.endsWith("@student.tuiasi.ro")) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText(
                    "Email must end with @student.tuiasi.ro"
            );

            return;
        }

        /**
         * Verifică dacă email-ul este gol.
         */
        if (email.isEmpty()) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText(
                    "Introdu email-ul mai intai!"
            );

            return;
        }

        try {

            FaceApiClient apiClient =
                    new FaceApiClient();

            String result =
                    apiClient.register(email).trim();

            /**
             * Verifică rezultatul înregistrării.
             */
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

            messageLabel.setText(
                    "Cannot connect to Face ID API!"
            );
        }
    }
}