package org.example.proiectpip2;

import fileUploader.account.UserAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Controller responsabil pentru
 * autentificarea utilizatorilor.
 *
 * Clasa gestionează:
 * autentificarea clasică,
 * autentificarea Face ID și
 * navigarea către pagina de înregistrare.
 */
public class LoginController {

    /**
     * Etichetă utilizată pentru afișarea
     * mesajelor de eroare sau succes.
     */
    @FXML
    private Label messageLabel;

    /**
     * Câmp pentru introducerea username-ului.
     */
    @FXML
    private TextField usernameField;

    /**
     * Câmp pentru introducerea parolei.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Gestionează autentificarea clasică
     * utilizând username și parolă.
     */
    @FXML
    public void handleLogin() {

        String username = usernameField.getText();

        String password = passwordField.getText();

        /**
         * Verifică dacă există câmpuri goale.
         */
        if (username.isEmpty() || password.isEmpty()) {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText("Empty fields!");

            return;
        }

        User user = UserService.findUser(username, password);

        /**
         * Verifică dacă utilizatorul există.
         */
        if (user != null) {

            messageLabel.setStyle("-fx-text-fill: green;");

            messageLabel.setText("Login successful!");

            UserAccount account =
                    UserService.loadUserAccountByEmail(user.getEmail());

            openChatBot(user.getEmail(), account);

        } else {

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText("Invalid credentials!");
        }
    }

    /**
     * Gestionează autentificarea
     * prin recunoaștere facială.
     */
    @FXML
    private void handleGoToFaceId() {

        try {

            FaceApiClient apiClient = new FaceApiClient();

            String result = apiClient.login();

            /**
             * Verifică rezultatul autentificării.
             */
            if (result == null || result.startsWith("LOGIN_FAILED")) {

                messageLabel.setStyle("-fx-text-fill: red;");

                messageLabel.setText("Face ID failed!");

            } else {

                String email = result.trim();

                User user =
                        UserService.ensureFaceUserByEmail(email);

                UserAccount account =
                        UserService.loadUserAccountByEmail(user.getEmail());

                messageLabel.setStyle("-fx-text-fill: green;");

                messageLabel.setText("Face ID success!");

                openChatBot(user.getEmail(), account);
            }

        } catch (Exception e) {

            e.printStackTrace();

            messageLabel.setStyle("-fx-text-fill: red;");

            messageLabel.setText("API connection error!");
        }
    }

    /**
     * Deschide aplicația ChatBot
     * după autentificarea utilizatorului.
     *
     * @param identityEmail email-ul utilizatorului
     * @param account contul utilizatorului
     */
    private void openChatBot(
            String identityEmail,
            UserAccount account
    ) {

        Stage stage =
                (Stage) usernameField.getScene().getWindow();

        stage.hide();

        SwingUtilities.invokeLater(() -> {

            JFrame frame =
                    new JFrame("SmartDocs - ChatBot");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(1200, 800);

            frame.setLocationRelativeTo(null);

            frame.setContentPane(
                    new fileUploader.ui.ChatBot(
                            identityEmail,
                            account
                    )
            );

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            frame.setVisible(true);
        });
    }

    /**
     * Navighează către pagina
     * de creare a unui cont nou.
     */
    @FXML
    private void handleGoToRegister() {

        try {

            Stage stage =
                    (Stage) usernameField.getScene().getWindow();

            Parent content = FXMLLoader.load(
                    getClass().getResource("register-view.fxml")
            );

            stage.getScene().setRoot(
                    HelloApplication.buildAnimatedRoot(content)
            );

            stage.setTitle("SmartDocs - Create Account");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}