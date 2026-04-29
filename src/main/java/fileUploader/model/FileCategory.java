package fileUploader.model;

import java.awt.*;

public enum FileCategory {

    IMAGES("Imagini", "\uD83D\uDDBC", new Color(236, 72, 153)),
    DOCUMENTS("Documente", "\uD83D\uDCDD", new Color(59, 130, 246)),
    PDFS("PDF-uri", "\uD83D\uDCC4", new Color(239, 68, 68)),
    ARCHIVES("Arhive", "\uD83D\uDDC2", new Color(245, 158, 11)),
    AUDIO("Audio", "\uD83C\uDFA7", new Color(139, 92, 246)),
    VIDEO("Video", "\uD83C\uDFAC", new Color(20, 184, 166)),
    CODE("Cod / Scripturi", "\uD83D\uDCBB", new Color(34, 197, 94)),
    OTHER("Altele", "\uD83D\uDCC1", new Color(148, 163, 184));

    private final String name;
    private final String icon;
    private final Color color;

    FileCategory(String name, String icon, Color color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public Color getColor() {
        return this.color;
    }
}
