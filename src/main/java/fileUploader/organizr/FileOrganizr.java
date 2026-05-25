package fileUploader.organizr;

import fileUploader.model.FileCategory;
import fileUploader.model.FileItem;

import java.io.File;
import java.util.Locale;

/**
 * Clasă utilitară pentru organizarea
 * și clasificarea fișierelor.
 *
 * Clasa permite:
 * determinarea extensiei,
 * detectarea categoriei și
 * transformarea unui fișier
 * într-un obiect FileItem.
 */
public final class FileOrganizr {

    /**
     * Constructor privat pentru a preveni
     * instanțierea clasei utilitare.
     */
    private FileOrganizr() {
    }

    /**
     * Transformă un fișier într-un obiect FileItem.
     *
     * @param file fișierul procesat
     * @return obiectul FileItem generat
     */
    public static FileItem toFileItem(File file) {
        String extension = getExtension(file.getName());
        FileCategory category = detectCategory(extension);
        return new FileItem(file, file.getName(), extension, category, false);
    }

    /**
     * Determină extensia unui fișier.
     *
     * @param fileName numele fișierului
     * @return extensia fișierului sau
     * "fără extensie" dacă nu există
     */
    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "fără extensie";
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    /**
     * Detectează categoria unui fișier
     * pe baza extensiei.
     *
     * @param extension extensia fișierului
     * @return categoria asociată
     */
    public static FileCategory detectCategory(String extension) {
        switch (extension.toLowerCase(Locale.ROOT)) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return FileCategory.IMAGES;

            case "pdf":
                return FileCategory.PDFS;

            case "doc":
            case "docx":
            case "txt":
                return FileCategory.DOCUMENTS;

            case "zip":
            case "rar":
                return FileCategory.ARCHIVES;

            case "mp3":
            case "wav":
                return FileCategory.AUDIO;

            case "mp4":
            case "avi":
                return FileCategory.VIDEO;

            default:
                return FileCategory.OTHER;
        }
    }
}