package fileUploader.organizr;

import fileUploader.model.FileCategory;
import fileUploader.model.FileItem;

import java.io.File;
import java.util.Locale;

public final class FileOrganizr {

    private FileOrganizr() {
    }

    public static FileItem toFileItem(File file) {
        String extension = getExtension(file.getName());
        FileCategory category = detectCategory(extension);
        return new FileItem(file, file.getName(), extension, category, false);
    }

    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "fără extensie";
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

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
