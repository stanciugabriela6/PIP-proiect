package fileUploader.ui;

import fileUploader.ai.RagApiClient;
import fileUploader.ai.UploadedDocsRegistry;
import javax.swing.SwingWorker;
import fileUploader.model.FileItem;
import fileUploader.organizr.FileOrganizr;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final UploadPanel uploadPanel;
    private final FileListPanel fileListPanel;
    private final List<FileItem> uploadedFiles;

    public MainFrame() {
        super("Document Upload");
        this.uploadedFiles = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        root.setBackground(AppColors.BG_DEEP);

        uploadPanel = new UploadPanel();
        fileListPanel = new FileListPanel();

        JPanel contentCard = new JPanel(new BorderLayout());
        contentCard.setBackground(AppColors.BG_SURFACE);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        contentCard.add(fileListPanel, BorderLayout.CENTER);

        JButton readyBtn = new JButton("Done") {
            private boolean hov = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hov = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? AppColors.SUCCESS.brighter() : AppColors.SUCCESS);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(8, 10, 18));
                g2.setFont(AppColors.font(Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        readyBtn.setContentAreaFilled(false);
        readyBtn.setBorderPainted(false);
        readyBtn.setFocusPainted(false);
        readyBtn.setOpaque(false);
        readyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        readyBtn.setPreferredSize(new Dimension(110, 36));
        readyBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(MainFrame.this.getContentPane());
            if (w != null) w.dispose();
        });

        JButton clearAllBtn = new JButton("Clear All Documents") {
            private boolean hov = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hov = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? AppColors.ERROR.brighter() : AppColors.ERROR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(AppColors.font(Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        clearAllBtn.setContentAreaFilled(false);
        clearAllBtn.setBorderPainted(false);
        clearAllBtn.setFocusPainted(false);
        clearAllBtn.setOpaque(false);
        clearAllBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearAllBtn.setPreferredSize(new Dimension(180, 36));
        clearAllBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Stergi TOATE documentele din baza RAG?\nAceasta actiune nu poate fi anulata.",
                    "Confirmare stergere", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            clearAllBtn.setEnabled(false);
            uploadPanel.setStatus("Se sterg documentele...", AppColors.WARNING);
            new SwingWorker<Void, Void>() {
                @Override protected Void doInBackground() throws Exception {
                    RagApiClient.clearDocuments();
                    UploadedDocsRegistry.clear();
                    return null;
                }
                @Override protected void done() {
                    clearAllBtn.setEnabled(true);
                    try {
                        get();
                        fileListPanel.setFiles(new java.util.ArrayList<>());
                        uploadedFiles.clear();
                        uploadPanel.setStatus("Toate documentele au fost sterse.", AppColors.SUCCESS);
                    } catch (Exception ex) {
                        uploadPanel.setStatus("Eroare: " + ex.getMessage(), AppColors.ERROR);
                    }
                }
            }.execute();
        });

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(clearAllBtn);
        footer.add(readyBtn);

        root.add(contentCard, BorderLayout.CENTER);
        root.add(uploadPanel, BorderLayout.SOUTH);
        root.add(footer, BorderLayout.NORTH);

        setContentPane(root);

        uploadPanel.getUploadButton().addActionListener(e -> openChooser());
        initDropAndPaste();
    }

    private void openChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Documents (*.pdf, *.txt)", "pdf", "txt"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            addFiles(List.of(chooser.getSelectedFiles()));
        }
    }

    private void initDropAndPaste() {
        TransferHandler handler = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport s) {
                boolean ok = s.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
                uploadPanel.setDropActive(ok);
                return ok;
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean importData(TransferSupport s) {
                uploadPanel.setDropActive(false);
                try {
                    List<File> files = (List<File>) s.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    addFiles(files);
                    return true;
                } catch (Exception e) {
                    uploadPanel.setStatus("Drop failed: " + safeMsg(e), new Color(255, 145, 145));
                    return false;
                }
            }
        };
        uploadPanel.setTransferHandler(handler);
    }

    private void addFiles(List<File> files) {
        List<File> valid = new ArrayList<>();
        List<String> rejected = new ArrayList<>();

        for (File f : files) {
            if (isAllowedType(f)) {
                valid.add(f);
                uploadedFiles.add(FileOrganizr.toFileItem(f));
            } else {
                rejected.add(f.getName());
            }
        }

        fileListPanel.setFiles(uploadedFiles);

        if (!rejected.isEmpty()) {
            uploadPanel.setStatus("Rejected (only .pdf/.txt): " + String.join(", ", rejected), new Color(255, 190, 110));
        }

        if (!valid.isEmpty()) ingestFilesAsync(valid);
    }

    private boolean isAllowedType(File file) {
        String lower = file.getName().toLowerCase();
        boolean extensionOk = lower.endsWith(".pdf") || lower.endsWith(".txt");
        if (!extensionOk) return false;

        try {
            String mime = Files.probeContentType(file.toPath());
            if (mime == null || mime.isBlank()) return true;
            if (lower.endsWith(".pdf")) return "application/pdf".equalsIgnoreCase(mime) || mime.toLowerCase().contains("pdf");
            return mime.toLowerCase().startsWith("text/") || mime.toLowerCase().contains("plain");
        } catch (Exception ignored) {
            return true;
        }
    }

    private void ingestFilesAsync(List<File> files) {
        uploadPanel.setUploading(true);
        uploadPanel.setStatus("Uploading " + files.size() + " file(s)...", AppColors.TEXT_PRIMARY);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                StringBuilder result = new StringBuilder();
                for (File file : files) {
                    try {
                        String lower = file.getName().toLowerCase();
                        String response = lower.endsWith(".pdf") ? RagApiClient.ingestPdf(file) : RagApiClient.ingestTxt(file);
                        UploadedDocsRegistry.add(file.getName());
                        result.append("OK: ").append(file.getName()).append(" -> ").append(response).append("\n");
                    } catch (Exception ex) {
                        result.append("ERR: ").append(file.getName()).append(" -> ").append(safeMsg(ex)).append("\n");
                    }
                }
                return result.toString();
            }

            @Override
            protected void done() {
                uploadPanel.setUploading(false);
                try {
                    String summary = get();
                    long okCount = summary.lines().filter(s -> s.startsWith("OK:")).count();
                    long errCount = summary.lines().filter(s -> s.startsWith("ERR:")).count();

                    if (errCount == 0) uploadPanel.setStatus("Ingestion complete: " + okCount + " success", new Color(140, 245, 170));
                    else uploadPanel.setStatus("Ingestion complete: " + okCount + " success, " + errCount + " failed", new Color(255, 190, 110));
                } catch (Exception ex) {
                    uploadPanel.setStatus("Ingestion failed: " + safeMsg(ex), new Color(255, 145, 145));
                }
            }
        }.execute();
    }

    private String safeMsg(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null) root = root.getCause();
        return root.getMessage() == null ? root.getClass().getSimpleName() : root.getMessage();
    }
}
