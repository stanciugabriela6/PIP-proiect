package fileUploader.model;

import java.io.File;

/**
 * Reprezintă un fișier încărcat în aplicația SmartDocs.
 * Clasa stochează informații despre fișier,
 * precum numele, extensia, categoria și
 * starea de selecție.
 */
public class FileItem {

    /**
     * Fișierul original.
     */
    private final File file;

    /**
     * Numele fișierului.
     */
    private final String fileName;

    /**
     * Extensia fișierului.
     */
    private final String extension;

    /**
     * Categoria fișierului.
     */
    private final FileCategory category;

    /**
     * Indică dacă fișierul este selectat.
     */
    private final boolean selected;

    /**
     * Creează un obiect de tip FileItem.
     *
     * @param file fișierul original
     * @param fileName numele fișierului
     * @param extension extensia fișierului
     * @param category categoria fișierului
     * @param selected starea de selecție
     */
    public FileItem(
            File file,
            String fileName,
            String extension,
            FileCategory category,
            boolean selected
    ) {

        this.file = file;
        this.fileName = fileName;
        this.extension = extension;
        this.category = category;
        this.selected = selected;
    }

    /**
     * Returnează fișierul original.
     *
     * @return fișierul original
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Returnează numele fișierului.
     *
     * @return numele fișierului
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returnează extensia fișierului.
     *
     * @return extensia fișierului
     */
    public String getExtension() {
        return this.extension;
    }

    /**
     * Returnează categoria fișierului.
     *
     * @return categoria fișierului
     */
    public FileCategory getCategory() {
        return this.category;
    }

    /**
     * Verifică dacă fișierul este selectat.
     *
     * @return true dacă este selectat,
     * false în caz contrar
     */
    public boolean isSelected() {
        return selected;
    }

}