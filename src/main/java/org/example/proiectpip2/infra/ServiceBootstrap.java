package org.example.proiectpip2.infra;

import fileUploader.ai.RagApiClient;
import org.example.proiectpip2.FaceApiClient;

import java.util.concurrent.CompletableFuture;

public final class ServiceBootstrap {

    private static volatile boolean started = false;
    private static CompletableFuture<BootstrapReport> warmUpFuture;

    private ServiceBootstrap() {
    }

    public static CompletableFuture<BootstrapReport> warmUpAsync() {
        synchronized (ServiceBootstrap.class) {
            if (started && warmUpFuture != null) {
                return warmUpFuture;
            }
            started = true;
            warmUpFuture = CompletableFuture.supplyAsync(ServiceBootstrap::warmUpNow);
            return warmUpFuture;
        }
    }

    public static BootstrapReport warmUpBlocking() {
        return warmUpAsync().join();
    }

    private static BootstrapReport warmUpNow() {
        StringBuilder errors = new StringBuilder();

        try {
            FaceApiClient.ensureServerRunning();
        } catch (Exception e) {
            appendError(errors, "Face API", e);
        }

        try {
            LocalInfraBootstrap.ensureInfraRunning();
        } catch (Exception e) {
            appendError(errors, "Local Infra (Ollama/Chroma)", e);
        }

        try {
            RagApiClient.ensureServiceRunning();
        } catch (Exception e) {
            appendError(errors, "RAG Service", e);
        }

        return new BootstrapReport(errors.isEmpty(), errors.toString().trim());
    }

    private static void appendError(StringBuilder errors, String area, Exception e) {
        String msg = "[ServiceBootstrap] " + area + " start failed: " + e.getMessage();
        System.err.println(msg);
        if (!errors.isEmpty()) {
            errors.append('\n');
        }
        errors.append(msg);
    }

    public record BootstrapReport(boolean success, String details) {
    }
}