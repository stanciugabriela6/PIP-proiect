package fileUploader.ui;

import java.awt.Color;
import java.awt.Font;

public final class AppColors {

    private AppColors() {}

    // Backgrounds
    public static final Color BG_DEEP    = new Color(8, 10, 18);
    public static final Color BG_SURFACE = new Color(14, 18, 30);
    public static final Color BG_CARD    = new Color(22, 27, 44);
    public static final Color BG_INPUT   = new Color(17, 22, 36);
    public static final Color BG_HOVER   = new Color(30, 38, 62);

    // Borders
    public static final Color BORDER_DEFAULT = new Color(38, 48, 72);
    public static final Color BORDER_FOCUS   = new Color(99, 135, 255, 160);

    // Text
    public static final Color TEXT_PRIMARY   = new Color(225, 230, 255);
    public static final Color TEXT_SECONDARY = new Color(110, 125, 165);
    public static final Color TEXT_MUTED     = new Color(60, 72, 100);

    // Accents
    public static final Color ACCENT          = new Color(99, 135, 255);
    public static final Color ACCENT_HOVER    = new Color(124, 158, 255);
    public static final Color ACCENT2         = new Color(0, 5, 100);
    public static final Color SELECTED_BG     = new Color(99, 135, 255, 28);
    public static final Color SELECTED_BORDER = new Color(99, 135, 255, 100);

    // Status
    public static final Color SUCCESS  = new Color(52, 211, 153);
    public static final Color WARNING  = new Color(251, 191, 36);
    public static final Color ERROR    = new Color(239, 68, 68);
    public static final Color VALIDATE = new Color(52, 211, 153);

    // Typography
    public static final String FONT_FAMILY = "Segoe UI";

    public static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }
}
