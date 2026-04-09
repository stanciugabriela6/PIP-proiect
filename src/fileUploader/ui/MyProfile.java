package fileUploader.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyProfile extends JPanel {

    String firstName;
    String lastName;
    String position;
    int age;
    int performanceScore;
    Image backgroundImg;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField positionField;
    private JTextField ageField;
    private JTextField scoreField;

    public MyProfile() {
        setLayout(null);
        setBackground(AppColors.BG_DEEP);
        setPreferredSize(new Dimension(900, 700));

        Image robotImg = new ImageIcon(getClass().getResource("/robot.png")).getImage();
        Image robotImg2 = new ImageIcon(getClass().getResource("/robot2.png")).getImage();
        Image robotImg3 = new ImageIcon(getClass().getResource("/robot3.png")).getImage();
        Image robotImg4 = new ImageIcon(getClass().getResource("/robot4.png")).getImage();
        Image robotImg5 = new ImageIcon(getClass().getResource("/robot5.png")).getImage();
        Image robotImg6 = new ImageIcon(getClass().getResource("/robot6.png")).getImage();
        Image robotImg7 = new ImageIcon(getClass().getResource("/robot7.png")).getImage();
        Image robotImg8 = new ImageIcon(getClass().getResource("/robot8.png")).getImage();
        backgroundImg = new ImageIcon(getClass().getResource("/accountBackground.jpg")).getImage();

        JButton btnAvatar1 = new JButton(new ImageIcon(robotImg.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar1.setBounds(310, 95, 85, 70);
        btnAvatar1.setBackground(AppColors.BG_CARD);
        btnAvatar1.setFocusPainted(false);
        btnAvatar1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar1.setOpaque(true);
        btnAvatar1.setContentAreaFilled(true);
        btnAvatar1.setBorderPainted(false);
        add(btnAvatar1);

        JButton btnAvatar2 = new JButton(new ImageIcon(robotImg2.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar2.setBounds(405, 95, 85, 70);
        btnAvatar2.setBackground(AppColors.BG_CARD);
        btnAvatar2.setFocusPainted(false);
        btnAvatar2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar2.setOpaque(true);
        btnAvatar2.setContentAreaFilled(true);
        btnAvatar2.setBorderPainted(false);
        add(btnAvatar2);

        JButton btnAvatar3 = new JButton(new ImageIcon(robotImg3.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar3.setBounds(500, 95, 85, 70);
        btnAvatar3.setBackground(AppColors.BG_CARD);
        btnAvatar3.setFocusPainted(false);
        btnAvatar3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar3.setOpaque(true);
        btnAvatar3.setContentAreaFilled(true);
        btnAvatar3.setBorderPainted(false);
        add(btnAvatar3);

        JButton btnAvatar4 = new JButton(new ImageIcon(robotImg4.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar4.setBounds(595, 95, 85, 70);
        btnAvatar4.setBackground(AppColors.BG_CARD);
        btnAvatar4.setFocusPainted(false);
        btnAvatar4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar4.setOpaque(true);
        btnAvatar4.setContentAreaFilled(true);
        btnAvatar4.setBorderPainted(false);
        add(btnAvatar4);

        JButton btnAvatar5 = new JButton(new ImageIcon(robotImg5.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar5.setBounds(310, 175, 85, 70);
        btnAvatar5.setBackground(AppColors.BG_CARD);
        btnAvatar5.setFocusPainted(false);
        btnAvatar5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar5.setOpaque(true);
        btnAvatar5.setContentAreaFilled(true);
        btnAvatar5.setBorderPainted(false);
        add(btnAvatar5);

        JButton btnAvatar6 = new JButton(new ImageIcon(robotImg6.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar6.setBounds(405, 175, 85, 70);
        btnAvatar6.setBackground(AppColors.BG_CARD);
        btnAvatar6.setFocusPainted(false);
        btnAvatar6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar6.setOpaque(true);
        btnAvatar6.setContentAreaFilled(true);
        btnAvatar6.setBorderPainted(false);
        add(btnAvatar6);

        JButton btnAvatar7 = new JButton(new ImageIcon(robotImg7.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar7.setBounds(500, 175, 85, 70);
        btnAvatar7.setBackground(AppColors.BG_CARD);
        btnAvatar7.setFocusPainted(false);
        btnAvatar7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar7.setOpaque(true);
        btnAvatar7.setContentAreaFilled(true);
        btnAvatar7.setBorderPainted(false);
        add(btnAvatar7);

        JButton btnAvatar8 = new JButton(new ImageIcon(robotImg8.getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
        btnAvatar8.setBounds(595, 175, 85, 70);
        btnAvatar8.setBackground(AppColors.BG_CARD);
        btnAvatar8.setFocusPainted(false);
        btnAvatar8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAvatar8.setOpaque(true);
        btnAvatar8.setContentAreaFilled(true);
        btnAvatar8.setBorderPainted(false);
        add(btnAvatar8);

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setBounds(315, 275, 90, 24);
        firstNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        firstNameLabel.setForeground(AppColors.TEXT_SECONDARY);
        add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(410, 275, 245, 24);
        firstNameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        firstNameField.setForeground(AppColors.TEXT_PRIMARY);
        firstNameField.setBackground(AppColors.BG_SURFACE);
        firstNameField.setCaretColor(AppColors.TEXT_PRIMARY);
        firstNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                new EmptyBorder(3, 8, 3, 8)));
        add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setBounds(315, 310, 90, 24);
        lastNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lastNameLabel.setForeground(AppColors.TEXT_SECONDARY);
        add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(410, 310, 245, 24);
        lastNameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lastNameField.setForeground(AppColors.TEXT_PRIMARY);
        lastNameField.setBackground(AppColors.BG_SURFACE);
        lastNameField.setCaretColor(AppColors.TEXT_PRIMARY);
        lastNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                new EmptyBorder(3, 8, 3, 8)));
        add(lastNameField);

        JLabel positionLabel = new JLabel("Position");
        positionLabel.setBounds(315, 345, 90, 24);
        positionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        positionLabel.setForeground(AppColors.TEXT_SECONDARY);
        add(positionLabel);

        positionField = new JTextField();
        positionField.setBounds(410, 345, 245, 24);
        positionField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        positionField.setForeground(AppColors.TEXT_PRIMARY);
        positionField.setBackground(AppColors.BG_SURFACE);
        positionField.setCaretColor(AppColors.TEXT_PRIMARY);
        positionField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                new EmptyBorder(3, 8, 3, 8)));
        add(positionField);

        JLabel ageLabel = new JLabel("Age");
        ageLabel.setBounds(315, 380, 90, 24);
        ageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ageLabel.setForeground(AppColors.TEXT_SECONDARY);
        add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(410, 380, 245, 24);
        ageField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ageField.setForeground(AppColors.TEXT_PRIMARY);
        ageField.setBackground(AppColors.BG_SURFACE);
        ageField.setCaretColor(AppColors.TEXT_PRIMARY);
        ageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                new EmptyBorder(3, 8, 3, 8)));
        add(ageField);

        JLabel scoreLabel = new JLabel("Performance");
        scoreLabel.setBounds(315, 415, 90, 24);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        scoreLabel.setForeground(AppColors.TEXT_SECONDARY);
        add(scoreLabel);

        scoreField = new JTextField();
        scoreField.setBounds(410, 415, 245, 24);
        scoreField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        scoreField.setForeground(AppColors.TEXT_PRIMARY);
        scoreField.setBackground(AppColors.BG_SURFACE);
        scoreField.setCaretColor(AppColors.TEXT_PRIMARY);
        scoreField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                new EmptyBorder(3, 8, 3, 8)));
        add(scoreField);

        JButton updatePosition = new JButton("Position");
        updatePosition.setBounds(325, 470, 85, 26);
        updatePosition.setBackground(AppColors.ACCENT);
        updatePosition.setForeground(Color.WHITE);
        updatePosition.setFocusPainted(false);
        updatePosition.setBorderPainted(false);
        updatePosition.setOpaque(true);
        updatePosition.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(updatePosition);

        JButton updateAge = new JButton("Age");
        updateAge.setBounds(415, 470, 85, 26);
        updateAge.setBackground(AppColors.ACCENT);
        updateAge.setForeground(Color.WHITE);
        updateAge.setFocusPainted(false);
        updateAge.setBorderPainted(false);
        updateAge.setOpaque(true);
        updateAge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(updateAge);

        JButton updateScore = new JButton("Score");
        updateScore.setBounds(505, 470, 85, 26);
        updateScore.setBackground(AppColors.ACCENT);
        updateScore.setForeground(Color.WHITE);
        updateScore.setFocusPainted(false);
        updateScore.setBorderPainted(false);
        updateScore.setOpaque(true);
        updateScore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(updateScore);

        JButton deleteUser = new JButton("Delete");
        deleteUser.setBounds(595, 470, 85, 26);
        deleteUser.setBackground(new Color(200, 50, 50));
        deleteUser.setForeground(Color.WHITE);
        deleteUser.setFocusPainted(false);
        deleteUser.setBorderPainted(false);
        deleteUser.setOpaque(true);
        deleteUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(deleteUser);
    }

    public void updatePerformanceScore(int performanceScore) {
        this.performanceScore = performanceScore;
    }

    public void updateAge(int age) {
        this.age = age;
    }

    public void updatePosition(String position) {
        this.position = position;
    }

    public void deleteAccount() {
        this.firstName = "";
        this.lastName = "";
        this.position = "";
        this.age = 0;
        this.performanceScore = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}