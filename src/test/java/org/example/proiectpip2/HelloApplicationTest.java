package org.example.proiectpip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelloApplicationTest {

    @Test
    void testCreateHelloApplication() {

        HelloApplication app = new HelloApplication();

        assertNotNull(app);
    }

    @Test
    void testHelloApplicationClassName() {

        HelloApplication app = new HelloApplication();

        assertEquals(
                "HelloApplication",
                app.getClass().getSimpleName()
        );
    }

    @Test
    void testHelloApplicationPackage() {

        HelloApplication app = new HelloApplication();

        assertTrue(
                app.getClass()
                        .getPackageName()
                        .contains("proiectpip2")
        );
    }

    @Test
    void testHelloApplicationInheritance() {

        HelloApplication app = new HelloApplication();

        assertNotNull(app.getClass().getSuperclass());
    }

    @Test
    void testBuildSceneMethodExists() {

        boolean exists = false;

        for (var method : HelloApplication.class.getDeclaredMethods()) {

            if (method.getName().equals("buildScene")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testBuildAnimatedRootMethodExists() {

        boolean exists = false;

        for (var method : HelloApplication.class.getDeclaredMethods()) {

            if (method.getName().equals("buildAnimatedRoot")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testMainMethodExists() {

        boolean exists = false;

        for (var method : HelloApplication.class.getDeclaredMethods()) {

            if (method.getName().equals("main")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }
}