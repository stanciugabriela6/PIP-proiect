package fileUploader;

import javax.swing.*;

import fileUploader.ui.ChatBot;

/**
 * Clasa principală a aplicației SmartDocs.
 *
 * Această clasă inițializează interfața grafică
 * și lansează aplicația ChatBot.
 */
public class AppLauncher {

    /**
     * Punctul de pornire al aplicației.
     *
     * Metoda configurează tema grafică
     * și creează fereastra principală.
     *
     * @param args argumentele liniei de comandă
     */
    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            /**
             * Fereastra principală a aplicației.
             */
            JFrame frame = new JFrame("SmartDocs — ChatBot");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(1000, 700);

            frame.setLocationRelativeTo(null);

            frame.setContentPane(new ChatBot());

            frame.setVisible(true);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
    }
}