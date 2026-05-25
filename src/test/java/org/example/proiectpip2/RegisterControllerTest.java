package org.example.proiectpip2;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterControllerTest {

    @BeforeAll
    static void initJavaFX() {

        new JFXPanel();
    }

    private void setPrivateField(
            Object object,
            String fieldName,
            Object value
    ) throws Exception {

        Field field = object.getClass().getDeclaredField(fieldName);

        field.setAccessible(true);

        field.set(object, value);
    }

    private RegisterController createController(
            String username,
            String email,
            String password,
            String confirmPassword,
            String role,
            Label messageLabel
    ) throws Exception {

        RegisterController controller = new RegisterController();

        TextField usernameField = new TextField(username);

        TextField emailField = new TextField(email);

        PasswordField passwordField = new PasswordField();
        passwordField.setText(password);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setText(confirmPassword);

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setValue(role);

        setPrivateField(
                controller,
                "messageLabel",
                messageLabel
        );

        setPrivateField(
                controller,
                "usernameField",
                usernameField
        );

        setPrivateField(
                controller,
                "emailField",
                emailField
        );

        setPrivateField(
                controller,
                "passwordField",
                passwordField
        );

        setPrivateField(
                controller,
                "confirmPasswordField",
                confirmPasswordField
        );

        setPrivateField(
                controller,
                "roleComboBox",
                roleComboBox
        );

        return controller;
    }

    @Test
    void testEmptyFields() throws Exception {

        Label messageLabel = new Label();

        RegisterController controller = createController(
                "",
                "",
                "",
                "",
                null,
                messageLabel
        );

        controller.handleRegister();

        assertEquals(
                "Complete all fields!",
                messageLabel.getText()
        );
    }

    @Test
    void testPasswordsDoNotMatch() throws Exception {

        Label messageLabel = new Label();

        RegisterController controller = createController(
                "andrei",
                "andrei@student.tuiasi.ro",
                "password123",
                "password999",
                "JUNIOR",
                messageLabel
        );

        controller.handleRegister();

        assertEquals(
                "Passwords do not match",
                messageLabel.getText()
        );
    }

    @Test
    void testShortPassword() throws Exception {

        Label messageLabel = new Label();

        RegisterController controller = createController(
                "andrei",
                "andrei@student.tuiasi.ro",
                "123",
                "123",
                "JUNIOR",
                messageLabel
        );

        controller.handleRegister();

        assertEquals(
                "Password must be at least 8 characters",
                messageLabel.getText()
        );
    }

    @Test
    void testInvalidEmail() throws Exception {

        Label messageLabel = new Label();

        RegisterController controller = createController(
                "andrei",
                "andrei@gmail.com",
                "password123",
                "password123",
                "JUNIOR",
                messageLabel
        );

        controller.handleRegister();

        assertEquals(
                "Email must end with @student.tuiasi.ro",
                messageLabel.getText()
        );
    }
}