package fileUploader.ui;

import fileUploader.account.UserAccount;
import fileUploader.ai.RagApiClient;
import fileUploader.ai.UploadedDocsRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatBot extends JPanel {

    private final JLabel introLabel;
    private final JTextArea textArea;
    private final Image backgroundImg;
    private final JLabel robotHeaderLabel;
    private final JPanel messagesPanel;
    private final JScrollPane messagesScroll;
    private final JButton sendBtn;
    private final JComboBox<String> docSelector;
    private final List<String> transcript = new ArrayList<>();
    private final List<String[]> conversationHistory = new ArrayList<>(); // [role, content]
    private static final int MAX_HISTORY = 6; // ultimele 3 perechi user/bot
    private final String identityEmail;

    private final UserAccount sharedUser;
    private final Image[] robotImgs = new Image[8];

    public ChatBot() {
        this("guest", new UserAccount("Guest", "", "User", 0, 0, 0));
    }




    public ChatBot(String identityEmail, UserAccount account) {
        this.identityEmail = identityEmail == null ? "guest" : identityEmail;
        this.sharedUser = account == null ? new UserAccount("Guest", "", "User", 0, 0, 0) : account;

        setLayout(new BorderLayout());

        backgroundImg = new ImageIcon(getClass().getResource("/background.jpg")).getImage();
        Image sendImg = new ImageIcon(getClass().getResource("/send-message.png")).getImage();
        Image uploadImg = new ImageIcon(getClass().getResource("/upload.png")).getImage();
        Image userImg = new ImageIcon(getClass().getResource("/user.png")).getImage();

        robotImgs[0] = new ImageIcon(getClass().getResource("/robot.png")).getImage();
        robotImgs[1] = new ImageIcon(getClass().getResource("/robot2.png")).getImage();
        robotImgs[2] = new ImageIcon(getClass().getResource("/robot3.png")).getImage();
        robotImgs[3] = new ImageIcon(getClass().getResource("/robot4.png")).getImage();
        robotImgs[4] = new ImageIcon(getClass().getResource("/robot5.png")).getImage();
        robotImgs[5] = new ImageIcon(getClass().getResource("/robot6.png")).getImage();
        robotImgs[6] = new ImageIcon(getClass().getResource("/robot7.png")).getImage();
        robotImgs[7] = new ImageIcon(getClass().getResource("/robot8.png")).getImage();

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setOpaque(false);

        int initialAvatarIndex = Math.max(0, Math.min(sharedUser.getSelectedAvatarIndex(), robotImgs.length - 1));
        robotHeaderLabel = new JLabel(new ImageIcon(robotImgs[initialAvatarIndex].getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        header.add(robotHeaderLabel);

        introLabel = new JLabel(buildTimeGreeting());
        introLabel.setFont(AppColors.font(Font.PLAIN, 16));
        introLabel.setForeground(AppColors.TEXT_PRIMARY);
        header.add(introLabel);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        userPanel.setOpaque(false);

        JButton copyAllBtn = new JButton("Copy All");
        copyAllBtn.setFocusPainted(false);
        copyAllBtn.setBackground(AppColors.BG_HOVER);
        copyAllBtn.setForeground(AppColors.TEXT_PRIMARY);
        copyAllBtn.setFont(AppColors.font(Font.PLAIN, 12));
        copyAllBtn.setBorderPainted(false);
        copyAllBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyAllBtn.addActionListener(e -> copyTranscript());

        docSelector = new JComboBox<>();
        docSelector.setPreferredSize(new Dimension(420, 30));
        docSelector.setPrototypeDisplayValue("All documents                                  ");
        docSelector.addItem("All documents");
        refreshDocSelector();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(header, BorderLayout.CENTER);
        topPanel.add(userPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setOpaque(false);
        messagesPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        messagesScroll = new JScrollPane(messagesPanel);
        messagesScroll.setOpaque(false);
        messagesScroll.getViewport().setOpaque(false);
        messagesScroll.setBorder(BorderFactory.createEmptyBorder());
        messagesScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(messagesScroll, BorderLayout.CENTER);

        JPanel inputBar = new JPanel(new BorderLayout(12, 0));
        inputBar.setOpaque(false);
        inputBar.setBorder(new EmptyBorder(12, 20, 20, 20));

        JButton uploadBtn = new JButton(new ImageIcon(uploadImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH)));
        styleIconButton(uploadBtn);
        uploadBtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parent, "Upload", true);
            dialog.setSize(980, 680);
            dialog.setLocationRelativeTo(parent);
            dialog.setContentPane(new MainFrame().getContentPane());
            dialog.setVisible(true);
            refreshDocSelector();
        });

        JButton userAccountBtn = new JButton(new ImageIcon(userImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        styleIconButton(userAccountBtn);
        userAccountBtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parent, "SmartDocs - My Profile", true);
            dialog.setSize(1000, 700);
            dialog.setLocationRelativeTo(parent);
            dialog.setContentPane(new MyProfile(sharedUser, identityEmail));
            dialog.setVisible(true);
            int idx = Math.max(0, Math.min(sharedUser.getSelectedAvatarIndex(), robotImgs.length - 1));
            robotHeaderLabel.setIcon(new ImageIcon(robotImgs[idx].getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
            introLabel.setText(buildTimeGreeting());
        });

        textArea = new JTextArea(4, 72);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(AppColors.font(Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(12, 14, 12, 14));
        textArea.setOpaque(true);
        textArea.setBackground(AppColors.BG_INPUT);
        textArea.setForeground(AppColors.TEXT_PRIMARY);
        textArea.setCaretColor(AppColors.ACCENT);

        textArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "send");
        textArea.getActionMap().put("send", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        textArea.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "newline");
        textArea.getActionMap().put("newline", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("\n");
            }
        });

        sendBtn = new JButton(new ImageIcon(sendImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH)));
        styleIconButton(sendBtn);
        sendBtn.addActionListener(e -> sendMessage());

        JPanel centerInput = new JPanel(new BorderLayout(8, 8));
        centerInput.setOpaque(false);
        centerInput.setPreferredSize(new Dimension(980, 140));
        centerInput.add(docSelector, BorderLayout.NORTH);

        JScrollPane inputScroll = new JScrollPane(textArea);
        inputScroll.setPreferredSize(new Dimension(980, 100));
        centerInput.add(inputScroll, BorderLayout.CENTER);

        inputBar.add(uploadBtn, BorderLayout.WEST);
        inputBar.add(centerInput, BorderLayout.CENTER);
        inputBar.add(sendBtn, BorderLayout.EAST);

        userPanel.add(copyAllBtn);
        userPanel.add(userAccountBtn, BorderLayout.EAST);

        add(inputBar, BorderLayout.SOUTH);
        addInitialAssistantMessages();
    }

    private String buildTimeGreeting() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        String name = displayName();

        String prefix;
        if (hour < 12) {
            prefix = "Good morning";
        } else if (hour < 18) {
            prefix = "Good afternoon";
        } else {
            prefix = "Good evening";
        }

        return prefix + ", " + name + "! How can I help you today?";
    }

    private String displayName() {
        String first = sharedUser.getFirstName() == null ? "" : sharedUser.getFirstName().trim();
        if (!first.isBlank()) {
            return first;
        }
        if (identityEmail != null && identityEmail.contains("@")) {
            return identityEmail.substring(0, identityEmail.indexOf('@'));
        }
        return "there";
    }

    private void addInitialAssistantMessages() {
        addMessageBubble("Bot", "Poti pune intrebari despre documentele indexate aici.", false, AppColors.BG_CARD, AppColors.TEXT_PRIMARY, true);
        addMessageBubble("Bot", "Incarca fisiere PDF sau TXT din butonul Upload, apoi selecteaza documentul din lista daca vrei cautare targetata.", false, AppColors.BG_CARD, AppColors.TEXT_PRIMARY, true);
    }

    private void refreshDocSelector() {
        String selected = (String) docSelector.getSelectedItem();
        docSelector.removeAllItems();
        docSelector.addItem("All documents");
        for (String doc : UploadedDocsRegistry.list()) {
            docSelector.addItem(doc);
        }
        if (selected != null) docSelector.setSelectedItem(selected);
    }

    private void sendMessage() {
        String msg = textArea.getText().trim();
        if (msg.isEmpty()) return;

        String selectedDoc = (String) docSelector.getSelectedItem();
        if ("All documents".equals(selectedDoc)) selectedDoc = null;

        introLabel.setVisible(false);
        textArea.setText("");
        addMessageBubble("You", msg, true, AppColors.ACCENT, Color.WHITE, true);

        setInputEnabled(false);
        JPanel typingRow = addMessageBubble("Bot", "typing...", false, AppColors.BG_CARD, AppColors.TEXT_PRIMARY, false);

        conversationHistory.add(new String[]{"user", msg});
        if (conversationHistory.size() > MAX_HISTORY) conversationHistory.remove(0);
        List<String[]> historySnapshot = new ArrayList<>(conversationHistory);

        String finalSelectedDoc = selectedDoc;
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return RagApiClient.query(msg, finalSelectedDoc, historySnapshot);
            }

            @Override
            protected void done() {
                removeTypingBubble(typingRow);
                try {
                    String answer = get();
                    conversationHistory.add(new String[]{"assistant", answer});
                    if (conversationHistory.size() > MAX_HISTORY) conversationHistory.remove(0);
                    addMessageBubble("Bot", answer, false, AppColors.BG_CARD, AppColors.TEXT_PRIMARY, true);
                } catch (Exception ex) {
                    String friendly = friendlyError(ex);
                    addMessageBubble("System", friendly, false, new Color(120, 30, 30), Color.WHITE, true);
                } finally {
                    setInputEnabled(true);
                }
            }
        }.execute();
    }

    private JPanel addMessageBubble(String author, String text, boolean rightAligned, Color bubbleBg, Color fg, boolean copyable) {
        JPanel row = new JPanel(new FlowLayout(rightAligned ? FlowLayout.RIGHT : FlowLayout.LEFT, 8, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        String safeText = text == null ? "" : text;
        String fullText = author + ": " + safeText;

        JTextArea content = new JTextArea(fullText);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);
        content.setForeground(fg);
        content.setFont(AppColors.font(Font.PLAIN, 13));

        int hPad = 32, vPad = 22;
        int panelW = ChatBot.this.getWidth();
        int bubbleW = (panelW > 200) ? Math.min(760, (int) (panelW * 0.68)) : 680;
        content.setSize(bubbleW - hPad, Short.MAX_VALUE);
        int textH = content.getPreferredSize().height;

        JPanel bubble = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
            }
        };
        bubble.setOpaque(false);
        bubble.setBackground(bubbleBg);
        bubble.setBorder(new EmptyBorder(11, 16, 11, 16));
        bubble.setPreferredSize(new Dimension(bubbleW, textH + vPad));
        bubble.add(content, BorderLayout.CENTER);

        if (copyable) {
            transcript.add(fullText);
            JPopupMenu menu = new JPopupMenu();
            JMenuItem copyItem = new JMenuItem("Copy Message");
            copyItem.addActionListener(e -> copyToClipboard(safeText));
            menu.add(copyItem);
            MouseAdapter menuOpener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) menu.show(e.getComponent(), e.getX(), e.getY());
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) menu.show(e.getComponent(), e.getX(), e.getY());
                }
            };
            bubble.addMouseListener(menuOpener);
            content.addMouseListener(menuOpener);
        }

        row.add(bubble);
        messagesPanel.add(row);
        messagesPanel.add(Box.createVerticalStrut(6));
        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = messagesScroll.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });

        return row;
    }

    private void removeTypingBubble(JPanel typingRow) {
        messagesPanel.remove(typingRow);
        int count = messagesPanel.getComponentCount();
        if (count > 0 && messagesPanel.getComponent(count - 1) instanceof Box.Filler) {
            messagesPanel.remove(count - 1);
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private void copyTranscript() {
        if (transcript.isEmpty()) return;
        copyToClipboard(String.join("\n", transcript));
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    private String friendlyError(Exception ex) {
        Throwable root = ex;
        while (root.getCause() != null) root = root.getCause();
        String msg = root.getMessage();
        if (msg == null || msg.isBlank()) return "Serviciul RAG nu a raspuns. Verifica daca este pornit si incearca din nou.";
        String lower = msg.toLowerCase();
        if (lower.contains("http 500") || lower.contains("http 400")) return "RAG a intors eroare la procesare. Verifica modelul local si documentele indexate.";
        if (lower.contains("timed out")) return "RAG a depasit timpul de raspuns. Incearca o intrebare mai scurta sau repeta cererea.";
        return "Eroare RAG: " + msg;
    }

    private void setInputEnabled(boolean enabled) {
        textArea.setEditable(enabled);
        sendBtn.setEnabled(enabled);
    }

    private void styleIconButton(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(44, 44));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
    }
}


