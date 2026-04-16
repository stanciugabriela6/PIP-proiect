package fileUploader.ui;

import fileUploader.account.UserAccount;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MyProfile extends JPanel {

    UserAccount user;
    Image backgroundImg;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField positionField;
    private JTextField ageField;
    private JTextField scoreField;

    ArrayList<JButton> buttons = new ArrayList<>();
    ArrayList<JLabel> labels = new ArrayList<>();
    ArrayList<JTextField> fields = new ArrayList<>();
    ArrayList<JButton> statusButtons = new ArrayList<>();

    public MyProfile(UserAccount user) {
        this.user = user;

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

        Image[] allRobots = {robotImg, robotImg2, robotImg3, robotImg4, robotImg5, robotImg6, robotImg7, robotImg8};
        int[][] positions = {{310, 95}, {405, 95}, {500, 95}, {595, 95}, {310, 175}, {405, 175}, {500, 175}, {595, 175}};

        for (int i = 0; i < 8; i++) {
            final int index = i;
            JButton btn = new JButton(new ImageIcon(allRobots[i].getScaledInstance(55, 55, Image.SCALE_SMOOTH)));
            btn.setBounds(positions[i][0], positions[i][1], 85, 70);
            buttons.add(btn);
            btn.addActionListener(e -> {
                resetBtn();
                btn.setBackground(AppColors.VALIDATE);
                user.setSelectedAvatarIndex(index);
            });
        }

        JLabel avatar = new JLabel("Choose your avatar");
        avatar.setBounds(315, -300, 900, 700);
        labels.add(avatar);

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setBounds(315, 275, 90, 24);
        labels.add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setEnabled(false);
        firstNameField.setBounds(410, 275, 245, 24);
        fields.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setBounds(315, 310, 90, 24);
        labels.add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setEnabled(false);
        lastNameField.setBounds(410, 310, 245, 24);
        fields.add(lastNameField);

        JLabel positionLabel = new JLabel("Position");
        positionLabel.setBounds(315, 345, 90, 24);
        labels.add(positionLabel);

        positionField = new JTextField();
        positionField.setBounds(410, 345, 245, 24);
        fields.add(positionField);

        JLabel ageLabel = new JLabel("Age");
        ageLabel.setBounds(315, 380, 90, 24);
        labels.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(410, 380, 245, 24);
        fields.add(ageField);

        JLabel scoreLabel = new JLabel("Performance");
        scoreLabel.setBounds(315, 415, 90, 24);
        labels.add(scoreLabel);

        scoreField = new JTextField();
        scoreField.setBounds(410, 415, 245, 24);
        fields.add(scoreField);

        setAccount();
        highlightSelectedAvatar();

        JButton updatePosition = new JButton("Position");
        updatePosition.setBounds(325, 470, 85, 26);
        statusButtons.add(updatePosition);
        updatePosition.addActionListener(e -> updatePosition());

        JButton updateAge = new JButton("Age");
        updateAge.setBounds(415, 470, 85, 26);
        statusButtons.add(updateAge);
        updateAge.addActionListener(e -> {
            try {
                updateAge();
            } catch (NumberFormatException ex) {
                showError("Age must be a number.");
            }
        });

        JButton updateScore = new JButton("Score");
        updateScore.setBounds(505, 470, 85, 26);
        statusButtons.add(updateScore);
        updateScore.addActionListener(e -> {
            try {
                updatePerformanceScore();
            } catch (NumberFormatException ex) {
                showError("Score must be a number.");
            }
        });

        JButton deleteUser = new JButton("Delete");
        deleteUser.setBounds(595, 470, 85, 26);
        deleteUser.setBackground(new Color(200, 50, 50));
        deleteUser.setForeground(Color.WHITE);
        deleteUser.setFocusPainted(false);
        deleteUser.setBorderPainted(false);
        deleteUser.setOpaque(true);
        deleteUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(deleteUser);
        deleteUser.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this account?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) deleteAccount();
        });

        setButtons();
        setFields();
        setLabels();
        setStatusButtons();
    }

    public void highlightSelectedAvatar() {
        resetBtn();
        int idx = user.getSelectedAvatarIndex();
        if (idx >= 0 && idx < buttons.size()) {
            buttons.get(idx).setBackground(AppColors.VALIDATE);
        }
    }

    public void setStatusButtons() {
        for (JButton btn : statusButtons) {
            btn.setBackground(AppColors.ACCENT);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(btn);
        }
    }

    public void setFields() {
        for (JTextField field : fields) {
            field.setFont(new Font("SansSerif", Font.PLAIN, 12));
            field.setForeground(AppColors.TEXT_PRIMARY);
            field.setBackground(AppColors.BG_SURFACE);
            field.setCaretColor(AppColors.TEXT_PRIMARY);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppColors.BORDER_DEFAULT),
                    new EmptyBorder(3, 8, 3, 8)));
            add(field);
        }
    }

    public void setLabels() {
        for (JLabel label : labels) {
            label.setFont(new Font("SansSerif", Font.PLAIN, 12));
            label.setForeground(AppColors.TEXT_SECONDARY);
            add(label);
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    public void setButtons() {
        for (JButton b : buttons) {
            b.setBackground(AppColors.BG_CARD);
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setBorderPainted(false);
            add(b);
        }
    }

    public void resetBtn() {
        for (JButton b : buttons) {
            b.setBackground(AppColors.BG_CARD);
        }
    }

    public void updatePerformanceScore() {
        String perfScore = scoreField.getText();
        int score = Integer.parseInt(perfScore);
        user.setPerformance(score);
    }

    public void updateAge() {
        String age = ageField.getText();
        int ageInt = Integer.parseInt(age);
        user.setAge(ageInt);
    }

    public void updatePosition() {
        String pos = positionField.getText();
        user.setPosition(pos);
    }

    public void deleteAccount() {
        user.setFirstName("");
        user.setLastName("");
        user.setPosition("");
        user.setAge(0);
        user.setPerformance(0);
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setText("");
        ageField.setText("");
        scoreField.setText("");
    }

    public void setAccount() {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        positionField.setText(user.getPosition());
        ageField.setText(String.valueOf(user.getAge()));
        scoreField.setText(String.valueOf(user.getPerformance()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}