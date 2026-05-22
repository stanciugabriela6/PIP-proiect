package fileUploader.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UploadPanel extends JPanel {

    private final JButton uploadButton;
    private final JPanel dropZone;
    private final JLabel dropTitle;
    private final JLabel dropSubtitle;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    public UploadPanel() {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);

        dropZone = new JPanel(new BorderLayout(0, 8));
        dropZone.setBorder(BorderFactory.createCompoundBorder(
                new DashedRoundBorder(AppColors.BORDER_DEFAULT, 1.8f, 12),
                new EmptyBorder(24, 20, 24, 20)
        ));
        dropZone.setBackground(AppColors.BG_CARD);

        dropTitle = new JLabel("⬆  Drop PDF / TXT here", SwingConstants.CENTER);
        dropTitle.setForeground(AppColors.TEXT_PRIMARY);
        dropTitle.setFont(AppColors.font(Font.BOLD, 15));

        dropSubtitle = new JLabel("or use Browse Files below", SwingConstants.CENTER);
        dropSubtitle.setForeground(AppColors.TEXT_SECONDARY);
        dropSubtitle.setFont(AppColors.font(Font.PLAIN, 12));

        JPanel dropTextWrap = new JPanel(new GridLayout(2, 1, 0, 6));
        dropTextWrap.setOpaque(false);
        dropTextWrap.add(dropTitle);
        dropTextWrap.add(dropSubtitle);

        dropZone.add(dropTextWrap, BorderLayout.CENTER);

        JPanel actions = new JPanel(new BorderLayout(10, 0));
        actions.setOpaque(false);

        uploadButton = createAccentButton("Browse Files  (.pdf, .txt)");

        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(AppColors.TEXT_SECONDARY);
        statusLabel.setFont(AppColors.font(Font.PLAIN, 12));

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);
        progressBar.setBackground(AppColors.BG_CARD);
        progressBar.setForeground(AppColors.ACCENT);
        progressBar.setBorderPainted(false);

        actions.add(uploadButton, BorderLayout.WEST);
        actions.add(statusLabel, BorderLayout.CENTER);

        add(dropZone, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        add(progressBar, BorderLayout.NORTH);
    }

    private static JButton createAccentButton(String label) {
        JButton btn = new JButton(label) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? AppColors.ACCENT_HOVER : AppColors.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(AppColors.font(Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(230, 38));
        return btn;
    }

    private static class DashedRoundBorder extends AbstractBorder {
        private final Color color;
        private final float stroke;
        private final int arc;

        DashedRoundBorder(Color color, float stroke, int arc) {
            this.color = color;
            this.stroke = stroke;
            this.arc = arc;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    0, new float[]{6, 4}, 0));
            g2.drawRoundRect(x + 1, y + 1, w - 2, h - 2, arc, arc);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) { return new Insets(2, 2, 2, 2); }
        @Override
        public boolean isBorderOpaque() { return false; }
    }

    public JButton getUploadButton() {
        return uploadButton;
    }

    public void setDropActive(boolean active) {
        Color border = active ? AppColors.ACCENT : AppColors.BORDER_DEFAULT;
        Color bg = active ? new Color(30, 42, 72) : AppColors.BG_CARD;
        dropZone.setBorder(BorderFactory.createCompoundBorder(
                new DashedRoundBorder(border, active ? 2f : 1.8f, 12),
                new EmptyBorder(24, 20, 24, 20)
        ));
        dropZone.setBackground(bg);
        dropTitle.setForeground(active ? AppColors.ACCENT : AppColors.TEXT_PRIMARY);
    }

    public void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    public void setUploading(boolean uploading) {
        progressBar.setVisible(uploading);
        uploadButton.setEnabled(!uploading);
    }
}
