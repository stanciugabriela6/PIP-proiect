package fileUploader.ai;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

/**
 * Client utilizat pentru comunicarea
 * cu serviciul RAG din aplicația SmartDocs.
 */
public final class RagApiClient {

    /**
     * URL-ul de bază al serviciului RAG.
     */
    private static final String BASE_URL = resolveBaseUrl();

    /**
     * Client HTTP utilizat pentru
     * trimiterea cererilor către API.
     */
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * Procesul asociat serviciului RAG.
     */
    private static Process serviceProcess = null;

    /**
     * Constructor privat pentru a preveni
     * instanțierea clasei utilitare.
     */
    private RagApiClient() {}

    /**
     * Verifică dacă serviciul RAG rulează.
     *
     * Dacă serviciul nu este pornit,
     * acesta va fi lansat automat.
     *
     * @throws Exception dacă serviciul
     * nu poate fi pornit
     */
    public static synchronized void ensureServiceRunning() throws Exception {
        if (isServiceUp()) return;

        File dir = findRagServiceDir();
        if (dir == null) throw new Exception("Nu s-a gasit directorul rag-service");

        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder pb = windows
                ? new ProcessBuilder("cmd", "/c", "mvnw.cmd", "spring-boot:run")
                : new ProcessBuilder("./mvnw", "spring-boot:run");
        pb.directory(dir);
        File log = new File(dir, "rag-service.log");
        pb.redirectOutput(ProcessBuilder.Redirect.to(log));
        pb.redirectError(ProcessBuilder.Redirect.to(log));
        serviceProcess = pb.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (serviceProcess != null && serviceProcess.isAlive()) serviceProcess.destroy();
        }));

        for (int i = 0; i < 45; i++) {
            Thread.sleep(1000);
            if (isServiceUp()) return;
            if (serviceProcess != null && !serviceProcess.isAlive()) {
                throw new Exception("rag-service s-a oprit la start. Verifica rag-service/rag-service.log");
            }
        }
        throw new Exception("rag-service nu a devenit disponibil in timp util");
    }

    /**
     * Caută directorul serviciului RAG.
     *
     * @return directorul serviciului
     * sau null dacă nu există
     */
    private static File findRagServiceDir() {
        File dir = new File(System.getProperty("user.dir"));
        for (int i = 0; i < 8 && dir != null; i++) {
            File candidate = new File(dir, "rag-service");
            if (new File(candidate, "pom.xml").exists()) return candidate;

            File[] children = dir.listFiles(File::isDirectory);
            if (children != null) {
                for (File child : children) {
                    File sub = new File(child, "rag-service");
                    if (new File(sub, "pom.xml").exists()) return sub;
                }
            }
            dir = dir.getParentFile();
        }
        return null;
    }

    /**
     * Trimite o întrebare către serviciul RAG.
     *
     * @param question întrebarea utilizatorului
     * @param selectedFile fișierul selectat
     * @return răspunsul serviciului
     * @throws Exception dacă apare o eroare
     */
    public static String query(String question, String selectedFile) throws Exception {
        return query(question, selectedFile, null);
    }

    /**
     * Trimite o întrebare către serviciul RAG
     * împreună cu istoricul conversației.
     *
     * @param question întrebarea utilizatorului
     * @param selectedFile fișierul selectat
     * @param history istoricul conversației
     * @return răspunsul serviciului
     * @throws Exception dacă apare o eroare
     */
    public static String query(String question, String selectedFile, List<String[]> history) throws Exception {
        ensureServiceRunning();

        StringBuilder payload = new StringBuilder();
        payload.append("{\"question\":\"").append(escapeJson(question)).append("\"");
        payload.append(",\"selectedFile\":").append(
                selectedFile == null || selectedFile.isBlank() ? "null" : "\"" + escapeJson(selectedFile) + "\"");
        payload.append(",\"history\":[");
        if (history != null && !history.isEmpty()) {
            for (int i = 0; i < history.size(); i++) {
                String[] entry = history.get(i);
                if (i > 0) payload.append(",");
                payload.append("{\"role\":\"").append(escapeJson(entry[0]))
                        .append("\",\"content\":\"").append(escapeJson(entry[1])).append("\"}");
            }
        }
        payload.append("]}");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/query"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(90))
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new RuntimeException("Serviciul RAG a raspuns cu HTTP " + response.statusCode() + ": " + response.body());
        }

        String answer = extractJsonString(response.body(), "answer");
        return (answer != null && !answer.isBlank()) ? answer : response.body();
    }

    /**
     * Încarcă un document PDF
     * în serviciul RAG.
     *
     * @param file fișierul PDF
     * @return răspunsul serviciului
     * @throws Exception dacă apare o eroare
     */
    public static String ingestPdf(File file) throws Exception {
        ensureServiceRunning();
        return multipartUpload("/ingest", file, "application/pdf");
    }

    /**
     * Încarcă un fișier text
     * în serviciul RAG.
     *
     * @param file fișierul text
     * @return răspunsul serviciului
     * @throws Exception dacă apare o eroare
     */
    public static String ingestTxt(File file) throws Exception {
        ensureServiceRunning();
        return multipartUpload("/ingest", file, "text/plain; charset=UTF-8");
    }

    /**
     * Șterge toate documentele
     * procesate de serviciul RAG.
     *
     * @throws Exception dacă apare o eroare
     */
    public static void clearDocuments() throws Exception {
        ensureServiceRunning();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/documents"))
                .timeout(Duration.ofSeconds(30))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new RuntimeException("Eroare la stergere: HTTP " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Verifică dacă serviciul RAG
     * este disponibil.
     *
     * @return true dacă serviciul rulează,
     * false în caz contrar
     */
    public static boolean isServiceUp() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/health"))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Escape-uiește caracterele speciale
     * pentru format JSON.
     *
     * @param s textul original
     * @return textul procesat
     */
    private static String escapeJson(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Extrage valoarea unui câmp
     * dintr-un răspuns JSON.
     *
     * @param json răspunsul JSON
     * @param key cheia căutată
     * @return valoarea extrasă
     */
    private static String extractJsonString(String json, String key) {
        String needle = "\"" + key + "\"";
        int k = json.indexOf(needle);
        if (k < 0) return null;

        int i = json.indexOf(':', k + needle.length());
        if (i < 0) return null;
        i++;

        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length() || json.charAt(i) != '"') return null;
        i++;

        StringBuilder sb = new StringBuilder();
        while (i < json.length()) {
            char c = json.charAt(i++);
            if (c == '\\') {
                if (i >= json.length()) break;
                char e = json.charAt(i++);
                switch (e) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'u':
                        if (i + 4 <= json.length()) {
                            sb.append((char) Integer.parseInt(json.substring(i, i + 4), 16));
                            i += 4;
                        }
                        break;
                    default: sb.append(e);
                }
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Încarcă un fișier utilizând
     * cereri multipart/form-data.
     *
     * @param endpoint endpoint-ul API
     * @param file fișierul încărcat
     * @param contentType tipul conținutului
     * @return răspunsul serviciului
     * @throws Exception dacă apare o eroare
     */
    private static String multipartUpload(String endpoint, File file, String contentType) throws Exception {
        String boundary = "----SmartDocsBoundary" + System.currentTimeMillis();
        byte[] fileBytes = readFile(file);
        byte[] prefix = (
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n" +
                        "Content-Type: " + contentType + "\r\n\r\n"
        ).getBytes(StandardCharsets.UTF_8);
        byte[] suffix = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);
        byte[] body = new byte[prefix.length + fileBytes.length + suffix.length];
        System.arraycopy(prefix, 0, body, 0, prefix.length);
        System.arraycopy(fileBytes, 0, body, prefix.length, fileBytes.length);
        System.arraycopy(suffix, 0, body, prefix.length + fileBytes.length, suffix.length);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .timeout(Duration.ofSeconds(120))
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new RuntimeException("Upload failed HTTP " + response.statusCode() + ": " + response.body());
        }
        return response.body();
    }

    /**
     * Citește conținutul unui fișier.
     *
     * @param file fișierul citit
     * @return conținutul fișierului
     * @throws IOException dacă apare o eroare
     */
    private static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Determină URL-ul de bază
     * al serviciului RAG.
     *
     * @return URL-ul serviciului
     */
    private static String resolveBaseUrl() {
        String byProperty = System.getProperty("rag.service.base-url");
        if (byProperty != null && !byProperty.isBlank()) return normalizeBaseUrl(byProperty);
        String byEnv = System.getenv("RAG_SERVICE_BASE_URL");
        if (byEnv != null && !byEnv.isBlank()) return normalizeBaseUrl(byEnv);
        return "http://localhost:8080/api/rag";
    }

    /**
     * Normalizează URL-ul serviciului.
     *
     * @param raw URL-ul original
     * @return URL-ul normalizat
     */
    private static String normalizeBaseUrl(String raw) {
        String trimmed = raw.trim();
        while (trimmed.endsWith("/")) trimmed = trimmed.substring(0, trimmed.length() - 1);
        return trimmed;
    }
}