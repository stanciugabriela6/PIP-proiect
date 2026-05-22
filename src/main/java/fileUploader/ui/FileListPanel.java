package fileUploader.ui;

import fileUploader.model.FileCategory;
import fileUploader.model.FileItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FileListPanel extends JPanel {

    private final DefaultListModel<FileItem> model;
    private final JList<FileItem> list;

    public FileListPanel() {
        setLayout(new BorderLayout());
        setBackground(AppColors.BG_SURFACE);

        model = new DefaultListModel<>();
        list = new JList<>(model);

        list.setCellRenderer(new FileItemRenderer());
        list.setBackground(AppColors.BG_SURFACE);
        list.setForeground(AppColors.TEXT_PRIMARY);
        list.setSelectionBackground(AppColors.SELECTED_BG);
        list.setSelectionForeground(AppColors.TEXT_PRIMARY);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(AppColors.BG_SURFACE);
        scrollPane.getViewport().setBackground(AppColors.BG_SURFACE);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setFiles(List<FileItem> files) {
        model.clear();

        for (FileItem f : files) {
            model.addElement(f);
        }
    }

    private static class FileItemRenderer extends JPanel implements ListCellRenderer<FileItem> {
        private final JLabel iconLabel = new JLabel();
        private final JLabel nameLabel = new JLabel();
        private final JLabel extLabel  = new JLabel();
        private Color accentColor = AppColors.ACCENT;
        private boolean selected = false;

        FileItemRenderer() {
            setLayout(new BorderLayout(10, 0));

            iconLabel.setFont(AppColors.font(Font.PLAIN, 15));
            nameLabel.setFont(AppColors.font(Font.PLAIN, 13));
            extLabel.setFont(AppColors.font(Font.BOLD, 10));

            JPanel left = new JPanel(new BorderLayout(8, 0));
            left.setOpaque(false);
            left.add(iconLabel, BorderLayout.WEST);
            left.add(nameLabel, BorderLayout.CENTER);

            add(left, BorderLayout.CENTER);
            add(extLabel, BorderLayout.EAST);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            Color bar = selected ? accentColor
                    : new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 90);
            g2.setColor(bar);
            g2.fillRect(0, 0, 3, getHeight());
            g2.dispose();
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends FileItem> list, FileItem file,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            FileCategory category = file.getCategory();
            accentColor = category.getColor();
            selected = isSelected;

            iconLabel.setText(category.getIcon() + "  ");
            iconLabel.setForeground(accentColor);

            nameLabel.setText(file.getFileName());
            nameLabel.setForeground(AppColors.TEXT_PRIMARY);

            extLabel.setText("  " + file.getExtension().toUpperCase() + "  ");
            extLabel.setForeground(accentColor);

            setBackground(isSelected ? AppColors.BG_HOVER
                    : (index % 2 == 0 ? AppColors.BG_CARD : AppColors.BG_SURFACE));
            setBorder(new EmptyBorder(9, 13, 9, 12));
            setOpaque(true);
            return this;
        }
    }
}