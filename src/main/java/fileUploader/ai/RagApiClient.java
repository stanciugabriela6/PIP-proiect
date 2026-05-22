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

public final class RagApiClient {

    private static final String BASE_URL = resolveBaseUrl();

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static Process serviceProcess = null;

    private RagApiClient() {}

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

    public static String query(String question, String selectedFile) throws Exception {
        return query(question, selectedFile, null);
    }

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

    public static String ingestPdf(File file) throws Exception {
        ensureServiceRunning();
        return multipartUpload("/ingest", file, "application/pdf");
    }

    public static String ingestTxt(File file) throws Exception {
        ensureServiceRunning();
        return multipartUpload("/ingest", file, "text/plain; charset=UTF-8");
    }

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

    private static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    private static String resolveBaseUrl() {
        String byProperty = System.getProperty("rag.service.base-url");
        if (byProperty != null && !byProperty.isBlank()) return normalizeBaseUrl(byProperty);
        String byEnv = System.getenv("RAG_SERVICE_BASE_URL");
        if (byEnv != null && !byEnv.isBlank()) return normalizeBaseUrl(byEnv);
        return "http://localhost:8080/api/rag";
    }

    private static String normalizeBaseUrl(String raw) {
        String trimmed = raw.trim();
        while (trimmed.endsWith("/")) trimmed = trimmed.substring(0, trimmed.length() - 1);
        return trimmed;
    }
}
