package fileUploader.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Gestionează registrul documentelor încărcate
 * în aplicația SmartDocs.
 *
 * Clasa permite:
 * adăugarea, ștergerea, listarea și
 * persistarea documentelor pe disc.
 */
public final class UploadedDocsRegistry {

    /**
     * Setul documentelor încărcate.
     */
    private static final Set<String> DOCS =
            new LinkedHashSet<>();

    /**
     * Fișierul utilizat pentru persistarea
     * registrului documentelor.
     */
    private static final Path PERSIST_FILE;

    /**
     * Inițializează locația fișierului
     * și încarcă documentele salvate.
     */
    static {

        PERSIST_FILE = Paths.get(
                System.getProperty("user.home"),
                ".smartdocs",
                "docs-registry.txt"
        );

        load();
    }

    /**
     * Constructor privat pentru a preveni
     * instanțierea clasei utilitare.
     */
    private UploadedDocsRegistry() {}

    /**
     * Adaugă un document în registru.
     *
     * Dacă documentul este valid și nu există deja,
     * acesta va fi salvat automat pe disc.
     *
     * @param fileName numele documentului
     */
    public static synchronized void add(
            String fileName
    ) {

        if (fileName != null
                && !fileName.isBlank()
                && DOCS.add(fileName)) {

            save();
        }
    }

    /**
     * Elimină un document din registru.
     *
     * @param fileName numele documentului
     */
    public static synchronized void remove(
            String fileName
    ) {

        if (DOCS.remove(fileName)) {

            save();
        }
    }

    /**
     * Șterge toate documentele
     * din registru.
     */
    public static synchronized void clear() {

        DOCS.clear();

        save();
    }

    /**
     * Returnează lista documentelor încărcate.
     *
     * @return lista documentelor
     */
    public static synchronized List<String> list() {

        return new ArrayList<>(DOCS);
    }

    /**
     * Încarcă documentele salvate
     * din fișierul de persistență.
     */
    private static void load() {

        try {

            Files.createDirectories(
                    PERSIST_FILE.getParent()
            );

            if (Files.exists(PERSIST_FILE)) {

                Files.readAllLines(
                                PERSIST_FILE,
                                StandardCharsets.UTF_8
                        ).stream()
                        .map(String::trim)
                        .filter(l -> !l.isBlank())
                        .forEach(DOCS::add);
            }

        } catch (IOException e) {

            System.err.println(
                    "[DocsRegistry] Could not load registry: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Salvează registrul documentelor pe disc.
     */
    private static void save() {

        try {

            Files.createDirectories(
                    PERSIST_FILE.getParent()
            );

            Files.write(
                    PERSIST_FILE,
                    DOCS,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            System.err.println(
                    "[DocsRegistry] Could not save registry: "
                            + e.getMessage()
            );
        }
    }
}