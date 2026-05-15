package org.example.proiectpip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FaceApiClientTest {

    @Test
    void testCreateFaceApiClient() {

        FaceApiClient client = new FaceApiClient();

        assertNotNull(client);
    }

    @Test
    void testMultipleInstances() {

        FaceApiClient client1 = new FaceApiClient();
        FaceApiClient client2 = new FaceApiClient();

        assertNotSame(client1, client2);
    }

    @Test
    void testClientClassName() {

        FaceApiClient client = new FaceApiClient();

        assertEquals(
                "FaceApiClient",
                client.getClass().getSimpleName()
        );
    }

    @Test
    void testClientPackage() {

        FaceApiClient client = new FaceApiClient();

        assertTrue(
                client.getClass().getPackageName()
                        .contains("proiectpip2")
        );
    }
    @Test
    void testFaceApiClientInheritance() {

        FaceApiClient client = new FaceApiClient();

        assertEquals(
                Object.class,
                client.getClass().getSuperclass()
        );
    }

    @Test
    void testHealthMethodExists() {

        boolean exists = false;

        for (var method : FaceApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("health")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testRegisterMethodExists() {

        boolean exists = false;

        for (var method : FaceApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("register")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testLoginMethodExists() {

        boolean exists = false;

        for (var method : FaceApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("login")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testEnsureServerRunningMethodExists() {

        boolean exists = false;

        for (var method : FaceApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("ensureServerRunning")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testFaceApiClientHasMethods() {

        assertTrue(
                FaceApiClient.class.getDeclaredMethods().length > 0
        );
    }
}