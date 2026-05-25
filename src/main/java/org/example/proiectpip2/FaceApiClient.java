package org.example.proiectpip2;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Client utilizat pentru comunicarea
 * cu API-ul de recunoaștere facială.
 *
 * Clasa permite:
 * pornirea serverului,
 * verificarea stării serverului,
 * înregistrarea utilizatorilor și
 * autentificarea prin Face ID.
 */
public class FaceApiClient {

    /**
     * URL-ul de bază al API-ului.
     */
    private static final String BASE_URL = "http://127.0.0.1:8000";

    /**
     * Calea către executabilul Python.
     */
    private static final String PYTHON_EXE = "C:\\msys64\\ucrt64\\bin\\python.exe";

    /**
     * Client HTTP utilizat pentru
     * trimiterea cererilor către API.
     */
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * Procesul asociat serverului Python.
     */
    private static Process serverProcess = null;

    /**
     * Verifică dacă serverul API rulează.
     *
     * @return true dacă serverul este activ,
     * false în caz contrar
     */
    private static boolean isServerRunning() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/health"))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return response.statusCode() == 200;

        } catch (Exception e) {

            return false;
        }
    }

    /**
     * Caută directorul modulului
     * de recunoaștere facială.
     *
     * @return directorul modulului
     * sau null dacă nu există
     */
    private static File findModuleDir() {
        File dir = new File(System.getProperty("user.dir"));

        for (int i = 0; i < 8; i++) {

            File candidate = new File(dir, "face_recognition_module");

            if (candidate.exists() && candidate.isDirectory()) {
                return candidate;
            }

            File[] children = dir.listFiles(File::isDirectory);

            if (children != null) {

                for (File child : children) {

                    File sub = new File(child, "face_recognition_module");

                    if (sub.exists() && sub.isDirectory()) {
                        return sub;
                    }
                }
            }

            dir = dir.getParentFile();

            if (dir == null) {
                break;
            }
        }

        return null;
    }

    /**
     * Verifică dacă serverul rulează.
     *
     * Dacă serverul nu este pornit,
     * acesta va fi lansat automat.
     *
     * @throws Exception dacă serverul
     * nu poate fi pornit
     */
    public static void ensureServerRunning() throws Exception {

        if (isServerRunning()) {
            return;
        }

        File moduleDir = findModuleDir();

        if (moduleDir == null) {
            throw new Exception("Nu s-a gasit directorul face_recognition_module");
        }

        ProcessBuilder pb = new ProcessBuilder(
                PYTHON_EXE,
                "-m",
                "uvicorn",
                "app:app",
                "--host",
                "127.0.0.1",
                "--port",
                "8000"
        );

        pb.directory(moduleDir);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);

        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        serverProcess = pb.start();

        /**
         * Așteaptă pornirea serverului.
         */
        for (int i = 0; i < 30; i++) {

            Thread.sleep(500);

            if (isServerRunning()) {
                return;
            }
        }

        throw new Exception("API-ul nu a pornit in timp util");
    }

    /**
     * Verifică starea serverului API.
     *
     * @return răspunsul serverului
     * @throws Exception dacă apare o eroare
     */
    public static String health() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return response.body();
    }

    /**
     * Înregistrează un utilizator nou
     * utilizând recunoaștere facială.
     *
     * @param email email-ul utilizatorului
     * @return răspunsul serverului
     * @throws Exception dacă apare o eroare
     */
    public String register(String email) throws Exception {

        ensureServerRunning();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/register"))
                .header("Content-Type", "text/plain")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(email))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return response.body();
    }

    /**
     * Autentifică utilizatorul
     * utilizând recunoaștere facială.
     *
     * @return răspunsul serverului
     * @throws Exception dacă apare o eroare
     */
    public String login() throws Exception {

        ensureServerRunning();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return response.body();
    }
}