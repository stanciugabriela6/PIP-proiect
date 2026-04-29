package fileUploader.model;

import java.io.File;

public class FileItem {

    private final File file;
    private final String fileName;
    private final String extension;
    private final FileCategory category;
    private final boolean selected;

    public FileItem(File file, String fileName, String extension,
                    FileCategory category, boolean selected) {
        this.file = file;
        this.fileName = fileName;
        this.extension = extension;
        this.category = category;
        this.selected = selected;
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getExtension() {
        return this.extension;
    }

    public FileCategory getCategory() {
        return this.category;
    }

    public boolean isSelected() {
        return selected;
    }

}
