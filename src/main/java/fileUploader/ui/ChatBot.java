package fileUploader.ui;

import fileUploader.account.UserAccount;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class ChatBot extends JPanel {

    private static final String[] GREETINGS = {
            "Let's get started!",
            "What are we working on today?",
            "Hey! I've got your back, what's the task?",
            "Ready when you are. What's the plan?",
            "Hello! Ready to get things done together?",
            "Hi! I'm here to help, what's up?"
    };

    private final JLabel introLabel;
    private final JTextArea textArea;
    private final Image backgroundImg;
    private final JLabel robotHeaderLabel;

    private final UserAccount sharedUser = new UserAccount("John", "Doe", "Developer", 25, 90, 1);

    private final Image[] robotImgs = new Image[8];

    public ChatBot() {
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

        String greeting = GREETINGS[new Random().nextInt(GREETINGS.length)];

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setOpaque(false);

        robotHeaderLabel = new JLabel(new ImageIcon(robotImgs[0].getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        header.add(robotHeaderLabel);

        introLabel = new JLabel(greeting);
        introLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        introLabel.setForeground(Color.WHITE);
        header.add(introLabel);

        JPanel inputBar = new JPanel(new BorderLayout(8, 0));
        inputBar.setOpaque(false);
        inputBar.setBorder(new EmptyBorder(12, 20, 20, 20));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        userPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        topPanel.add(header, BorderLayout.CENTER);
        topPanel.add(userPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JButton uploadBtn = new JButton(new ImageIcon(uploadImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH)));
        styleIconButton(uploadBtn);
        uploadBtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            JDialog dialog = new JDialog(parent, "Upload", true);
            dialog.setSize(800, 500);
            dialog.setLocationRelativeTo(parent);

            dialog.setContentPane(new MainFrame().getContentPane());
            dialog.setVisible(true);
        });

        JButton userAccount = new JButton(new ImageIcon(userImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        styleIconButton(userAccount);
        userAccount.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            JDialog dialog = new JDialog(parent, "SmartDocs — My Profile", true);
            dialog.setSize(1000, 700);
            dialog.setLocationRelativeTo(parent);

            dialog.setContentPane(new MyProfile(sharedUser));
            dialog.setVisible(true);

            int idx = sharedUser.getSelectedAvatarIndex();
            robotHeaderLabel.setIcon(new ImageIcon(robotImgs[idx].getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        });

        textArea = new JTextArea(2, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        textArea.setOpaque(true);
        textArea.setBackground(AppColors.TEXT_SECONDARY);

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

        JButton sendBtn = new JButton(new ImageIcon(sendImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH)));
        styleIconButton(sendBtn);
        sendBtn.addActionListener(e -> sendMessage());

        inputBar.add(uploadBtn, BorderLayout.WEST);
        inputBar.add(new JScrollPane(textArea), BorderLayout.CENTER);
        inputBar.add(sendBtn, BorderLayout.EAST);

        userPanel.add(userAccount, BorderLayout.EAST);

        add(inputBar, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String msg = textArea.getText().trim();

        if (msg.isEmpty()) {
            return;
        }
        introLabel.setVisible(false);
        textArea.setText("");
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
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}