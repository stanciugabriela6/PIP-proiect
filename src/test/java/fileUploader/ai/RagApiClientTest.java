package fileUploader.ai;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class RagApiClientTest {

    @Test
    void testClassExists() {

        assertNotNull(RagApiClient.class);
    }

    @Test
    void testClassIsFinal() {

        assertTrue(
                Modifier.isFinal(RagApiClient.class.getModifiers())
        );
    }

    @Test
    void testConstructorIsPrivate() {

        assertEquals(
                1,
                RagApiClient.class.getDeclaredConstructors().length
        );

        assertTrue(
                Modifier.isPrivate(
                        RagApiClient.class
                                .getDeclaredConstructors()[0]
                                .getModifiers()
                )
        );
    }

    @Test
    void testHasQueryMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("query")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasIngestPdfMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("ingestPdf")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasIngestTxtMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("ingestTxt")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasClearDocumentsMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("clearDocuments")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasIsServiceUpMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("isServiceUp")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasEscapeJsonMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("escapeJson")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasExtractJsonStringMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("extractJsonString")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasMultipartUploadMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("multipartUpload")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasResolveBaseUrlMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("resolveBaseUrl")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testHasNormalizeBaseUrlMethod() {

        boolean exists = false;

        for (Method method : RagApiClient.class.getDeclaredMethods()) {

            if (method.getName().equals("normalizeBaseUrl")) {
                exists = true;
                break;
            }
        }

        assertTrue(exists);
    }

    @Test
    void testClassPackageName() {

        assertTrue(
                RagApiClient.class
                        .getPackageName()
                        .contains("fileUploader.ai")
        );
    }

    @Test
    void testClassSimpleName() {

        assertEquals(
                "RagApiClient",
                RagApiClient.class.getSimpleName()
        );
    }
}