package org.example.proiectpip2.infra;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class LocalInfraBootstrap {
    private static final String CHROMA_CONTAINER = "smartdocs-chroma";
    private static final String CHROMA_IMAGE = "chromadb/chroma:0.4.24";
    private static final String PYTHON_EXE = resolvePythonExe();

    private static final int CHROMA_PORT = 8001;
    private static final String CHROMA_HEALTH_URL = "http://127.0.0.1:" + CHROMA_PORT + "/api/v1/heartbeat";

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    private static Process ollamaProcess;
    private static Process chromaProcess;

    private LocalInfraBootstrap() {}

    private static String resolvePythonExe() {
        // 1. Check explicit env override
        String fromEnv = System.getenv("SMARTDOCS_PYTHON");
        if (fromEnv != null && !fromEnv.isBlank() && new File(fromEnv).exists()) return fromEnv;

        // 2. Known fixed paths (common Python installs on Windows)
        String localAppData = System.getenv("LOCALAPPDATA");
        String[] fixed = {
            "C:\\msys64\\ucrt64\\bin\\python.exe",
            "C:\\msys64\\mingw64\\bin\\python.exe",
            localAppData != null ? localAppData + "\\Programs\\Python\\Python312\\python.exe" : null,
            localAppData != null ? localAppData + "\\Programs\\Python\\Python311\\python.exe" : null,
            localAppData != null ? localAppData + "\\Programs\\Python\\Python310\\python.exe" : null,
            "C:\\Python312\\python.exe",
            "C:\\Python311\\python.exe",
            "C:\\Python310\\python.exe",
        };
        for (String path : fixed) {
            if (path != null && new File(path).exists()) return path;
        }

        // 3. Fall back to PATH
        for (String cmd : new String[]{"python3", "python"}) {
            if (commandExists(cmd)) return cmd;
        }

        System.err.println("[InfraBootstrap] Python not found. Set SMARTDOCS_PYTHON env var to your python.exe path.");
        return "python";
    }

    public static void ensureInfraRunning() {
        ensureOllamaRunning();
        ensureChromaRunning();
    }

    private static void ensureOllamaRunning() {
        if (!isUp("http://localhost:11434/api/tags")) {
            if (!commandExists("ollama")) {
                System.err.println("[InfraBootstrap] ollama not found in PATH.");
                return;
            }

            try {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "ollama", "serve");
                pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
                pb.redirectError(ProcessBuilder.Redirect.DISCARD);
                ollamaProcess = pb.start();
            } catch (Exception e) {
                System.err.println("[InfraBootstrap] Cannot start ollama serve: " + e.getMessage());
                return;
            }

            waitFor("http://localhost:11434/api/tags", 45);
        }

        ensureOllamaModel("nomic-embed-text");
        ensureOllamaModel(resolveChatModel());
    }

    private static String resolveChatModel() {
        String fromEnv = System.getenv("OLLAMA_CHAT_MODEL");
        return (fromEnv == null || fromEnv.isBlank()) ? "llama3.2" : fromEnv.trim();
    }

    private static void ensureChromaRunning() {
        if (isUp(CHROMA_HEALTH_URL)) return;

        if (startChromaWithPython()) {
            waitFor(CHROMA_HEALTH_URL, 60);
            if (isUp(CHROMA_HEALTH_URL)) return;
        }

        if (!commandExists("docker")) {
            System.err.println("[InfraBootstrap] docker not found in PATH (needed for Chroma fallback).");
            return;
        }

        try {
            if (containerExists(CHROMA_CONTAINER)) {
                String image = containerImage(CHROMA_CONTAINER);
                if (image == null || !image.equalsIgnoreCase(CHROMA_IMAGE)) {
                    run("docker", "rm", "-f", CHROMA_CONTAINER);
                    run("docker", "run", "-d", "--name", CHROMA_CONTAINER, "-p", CHROMA_PORT + ":8000", CHROMA_IMAGE);
                } else {
                    run("docker", "start", CHROMA_CONTAINER);
                }
            } else {
                run("docker", "run", "-d", "--name", CHROMA_CONTAINER, "-p", CHROMA_PORT + ":8000", CHROMA_IMAGE);
            }
        } catch (Exception e) {
            System.err.println("[InfraBootstrap] Cannot start Chroma container: " + e.getMessage());
            return;
        }

        waitFor(CHROMA_HEALTH_URL, 60);
    }

    private static boolean startChromaWithPython() {
        String[][] attempts = new String[][] {
                {PYTHON_EXE, "-m", "chroma", "run", "--host", "127.0.0.1", "--port", String.valueOf(CHROMA_PORT)},
                {PYTHON_EXE, "-m", "chromadb.cli.cli", "run", "--host", "127.0.0.1", "--port", String.valueOf(CHROMA_PORT)}
        };

        for (String[] cmd : attempts) {
            try {
                if (!new File(PYTHON_EXE).exists()) return false;
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
                pb.redirectError(ProcessBuilder.Redirect.DISCARD);
                chromaProcess = pb.start();
                Thread.sleep(1500);
                if (chromaProcess.isAlive()) {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (chromaProcess != null && chromaProcess.isAlive()) chromaProcess.destroy();
                    }));
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        System.err.println("[InfraBootstrap] Could not start Chroma from Python. Install with: " + PYTHON_EXE + " -m pip install chromadb");
        return false;
    }

    private static boolean isUp(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(2)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception e) {
            return false;
        }
    }

    private static void waitFor(String url, int seconds) {
        for (int i = 0; i < seconds; i++) {
            if (isUp(url)) return;
            try { Thread.sleep(1000); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); return; }
        }
    }

    private static boolean commandExists(String cmd) {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "where", cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            int exit = p.waitFor();
            return exit == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean containerExists(String name) {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "ps", "-a", "--filter", "name=^/" + name + "$", "--format", "{{.Names}}");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String out;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                out = reader.readLine();
            }
            p.waitFor();
            return out != null && out.trim().equals(name);
        } catch (Exception e) {
            return false;
        }
    }

    private static String containerImage(String name) {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "inspect", "--format", "{{.Config.Image}}", name);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String out;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                out = reader.readLine();
            }
            p.waitFor();
            return out == null ? null : out.trim();
        } catch (Exception e) {
            return null;
        }
    }

    private static void run(String... cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
    }

    private static void ensureOllamaModel(String model) {
        try {
            ProcessBuilder listPb = new ProcessBuilder("ollama", "list");
            listPb.redirectErrorStream(true);
            Process list = listPb.start();
            StringBuilder out = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(list.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) out.append(line).append('\n');
            }
            list.waitFor();
            if (out.toString().contains(model)) return;

            ProcessBuilder pullPb = new ProcessBuilder("ollama", "pull", model);
            pullPb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            pullPb.redirectError(ProcessBuilder.Redirect.DISCARD);
            Process pull = pullPb.start();
            pull.waitFor();
        } catch (Exception e) {
            System.err.println("[InfraBootstrap] Cannot ensure Ollama model " + model + ": " + e.getMessage());
        }
    }
}
