package fileUploader.ui;

import fileUploader.model.FileCategory;
import fileUploader.model.FileItem;

import javax.swing.*;
import javax.swing.border.Border;
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

    private static class FileItemRenderer extends DefaultListCellRenderer {

        private static final Border DEFAULT_BORDER =
                BorderFactory.createEmptyBorder(6, 10, 6, 10);

        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
            );

            FileItem file = (FileItem) value;
            FileCategory category = file.getCategory();

            label.setOpaque(true);
            label.setText(
                    category.getIcon() + " " + file.getFileName() +
                            " (" + file.getExtension() + ")"
            );

            if (isSelected) {
                label.setBackground(AppColors.SELECTED_BG);
                label.setForeground(AppColors.TEXT_PRIMARY);
                label.setBorder(BorderFactory.createLineBorder(AppColors.SELECTED_BORDER));
            } else {
                label.setBackground(AppColors.BG_CARD);
                label.setForeground(category.getColor());
                label.setBorder(DEFAULT_BORDER);
            }

            return label;
        }
    }
}