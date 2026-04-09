package fileUploader;

import fileUploader.ui.MainFrame;

import javax.swing.*;

//public class AppLauncher {
//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            MainFrame frame = new MainFrame();
//            frame.setVisible(true);
//        });
//    }
//}
import fileUploader.ui.ChatBot;
import fileUploader.ui.MyProfile;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ChatBot Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new MyProfile());
            frame.setVisible(true);
        });
    }
}