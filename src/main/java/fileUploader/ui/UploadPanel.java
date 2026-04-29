package fileUploader.ui;

import javax.swing.*;
import java.awt.*;

public class UploadPanel extends JPanel {

    private final JButton uploadButton;

    public UploadPanel() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Drag & Drop or Upload");
        label.setHorizontalAlignment(JLabel.CENTER);
        uploadButton = new JButton("Upload");

        setBackground(AppColors.BG_SURFACE);
        setBorder(BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT));

        label.setForeground(AppColors.TEXT_SECONDARY);

        uploadButton.setBackground(AppColors.ACCENT);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setOpaque(true);
        uploadButton.setBorderPainted(false);
        uploadButton.setContentAreaFilled(true);
        uploadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add(label, BorderLayout.CENTER);
        add(uploadButton, BorderLayout.SOUTH);
    }

    public JButton getUploadButton() {
        return uploadButton;
    }
}