package fileUploader.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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

    public ChatBot() {
        setLayout(new BorderLayout());

        backgroundImg = new ImageIcon(getClass().getResource("/background.jpg")).getImage();
        Image sendImg = new ImageIcon(getClass().getResource("/send-message.png")).getImage();
        Image uploadImg = new ImageIcon(getClass().getResource("/upload.png")).getImage();
        Image robotImg = new ImageIcon(getClass().getResource("/robot.png")).getImage();
        Image robotImg2 = new ImageIcon(getClass().getResource("/robot2.png")).getImage();
        Image robotImg3 = new ImageIcon(getClass().getResource("/robot3.png")).getImage();
        Image robotImg4 = new ImageIcon(getClass().getResource("/robot4.png")).getImage();
        Image robotImg5 = new ImageIcon(getClass().getResource("/robot5.png")).getImage();
        Image robotImg6 = new ImageIcon(getClass().getResource("/robot6.png")).getImage();
        Image robotImg7 = new ImageIcon(getClass().getResource("/robot7.png")).getImage();
        Image robotImg8 = new ImageIcon(getClass().getResource("/robot8.png")).getImage();
        Image userImg = new ImageIcon(getClass().getResource("/user.png")).getImage();

        String greeting = GREETINGS[new Random().nextInt(GREETINGS.length)];

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setOpaque(false);
        header.add(new JLabel(new ImageIcon(robotImg.getScaledInstance(60, 60, Image.SCALE_SMOOTH))));

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

        textArea = new JTextArea(2, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        textArea.setOpaque(true);
        textArea.setBackground(AppColors.TEXT_SECONDARY);

        JButton sendBtn = new JButton(new ImageIcon(sendImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH)));
        styleIconButton(sendBtn);
        sendBtn.addActionListener(e -> {
            introLabel.setVisible(false);
            textArea.setText("");
        });

        inputBar.add(uploadBtn, BorderLayout.WEST);
        inputBar.add(new JScrollPane(textArea), BorderLayout.CENTER);
        inputBar.add(sendBtn, BorderLayout.EAST);

        userPanel.add(userAccount, BorderLayout.EAST);

        add(inputBar, BorderLayout.SOUTH);
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