package fileUploader.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class UploadedDocsRegistry {

    private static final Set<String> DOCS = new LinkedHashSet<>();
    private static final Path PERSIST_FILE;

    static {
        PERSIST_FILE = Paths.get(System.getProperty("user.home"), ".smartdocs", "docs-registry.txt");
        load();
    }

    private UploadedDocsRegistry() {}

    public static synchronized void add(String fileName) {
        if (fileName != null && !fileName.isBlank() && DOCS.add(fileName)) {
            save();
        }
    }

    public static synchronized void remove(String fileName) {
        if (DOCS.remove(fileName)) save();
    }

    public static synchronized void clear() {
        DOCS.clear();
        save();
    }

    public static synchronized List<String> list() {
        return new ArrayList<>(DOCS);
    }

    private static void load() {
        try {
            Files.createDirectories(PERSIST_FILE.getParent());
            if (Files.exists(PERSIST_FILE)) {
                Files.readAllLines(PERSIST_FILE, StandardCharsets.UTF_8).stream()
                        .map(String::trim)
                        .filter(l -> !l.isBlank())
                        .forEach(DOCS::add);
            }
        } catch (IOException e) {
            System.err.println("[DocsRegistry] Could not load registry: " + e.getMessage());
        }
    }

    private static void save() {
        try {
            Files.createDirectories(PERSIST_FILE.getParent());
            Files.write(PERSIST_FILE, DOCS, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("[DocsRegistry] Could not save registry: " + e.getMessage());
        }
    }
}