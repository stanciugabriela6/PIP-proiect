package fileUploader.ui;

import fileUploader.model.FileItem;
import fileUploader.organizr.FileOrganizr;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final UploadPanel uploadPanel;
    private final FileListPanel fileListPanel;
    private final List<FileItem> uploadedFiles;

    public MainFrame() {
        super("File Upload Manager");
        this.uploadedFiles = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(AppColors.BG_DEEP);

        uploadPanel = new UploadPanel();
        fileListPanel = new FileListPanel();

        root.add(uploadPanel, BorderLayout.SOUTH);
        root.add(fileListPanel, BorderLayout.CENTER);

        setContentPane(root);

        uploadPanel.getUploadButton().addActionListener(e -> openChooser());
        init();
    }

    private void openChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            addFiles(List.of(chooser.getSelectedFiles()));
        }
    }

    private void init() {
        TransferHandler handler = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport s) {
                return s.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean importData(TransferSupport s) {
                try {
                    List<File> files = (List<File>) s.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    addFiles(files);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        };

        uploadPanel.setTransferHandler(handler);
    }

    private void addFiles(List<File> files) {
        for (File f : files) {
            uploadedFiles.add(FileOrganizr.toFileItem(f));
        }
        fileListPanel.setFiles(uploadedFiles);
    }
}