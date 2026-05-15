package org.example.proiectpip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    @Test
    void testCreateLoginController() {

        LoginController controller = new LoginController();

        assertNotNull(controller);
    }

    @Test
    void testLoginControllerClassName() {

        LoginController controller = new LoginController();

        assertEquals(
                "LoginController",
                controller.getClass().getSimpleName()
        );
    }

    @Test
    void testLoginControllerPackage() {

        LoginController controller = new LoginController();

        assertTrue(
                controller.getClass()
                        .getPackageName()
                        .contains("proiectpip2")
        );
    }

    @Test
    void testTwoDifferentControllers() {

        LoginController controller1 = new LoginController();
        LoginController controller2 = new LoginController();

        assertNotSame(controller1, controller2);
    }
    @Test
    void testControllerObjectsAreIndependent() {

        LoginController controller1 = new LoginController();
        LoginController controller2 = new LoginController();

        assertNotEquals(
                controller1.hashCode(),
                controller2.hashCode()
        );
    }

    @Test
    void testControllerInheritance() {

        LoginController controller = new LoginController();

        assertEquals(
                Object.class,
                controller.getClass().getSuperclass()
        );
    }

    @Test
    void testControllerHasHandleLoginMethod() {

        boolean exists = false;

        for (var method : LoginController.class.getDeclaredMethods()) {

            if (method.getName().equals("handleLogin")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testControllerHasFaceIdMethod() {

        boolean exists = false;

        for (var method : LoginController.class.getDeclaredMethods()) {

            if (method.getName().equals("handleGoToFaceId")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testControllerHasRegisterNavigationMethod() {

        boolean exists = false;

        for (var method : LoginController.class.getDeclaredMethods()) {

            if (method.getName().equals("handleGoToRegister")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }
}