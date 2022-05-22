package yutong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.sql.Connection;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import static yutong.BODY.BODY.BODY;
//import static yutong.tools.Allow.allow;
import static yutong.tools.DBManager.connectToDatabase;

public class MainFrame extends JFrame {

    public static void MainFrame(JFrame loadingFrame) {
        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/icon.png").getImage().getScaledInstance(40, -1, Image.SCALE_SMOOTH));

        new SwingWorker() {
            @Override
            protected Integer doInBackground() throws Exception {
//                if (allow()) {
                Connection connection = connectToDatabase();
                JFrame FRAME = new JFrame("");
                FRAME.getContentPane().setBackground(Color.white);

                FRAME.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                FRAME.setIconImage(icon.getImage());

                JPanel BODY = BODY(connection);
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                FRAME.getContentPane().add(new Header(BODY, connection), BorderLayout.PAGE_START);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                FRAME.getContentPane().add(BODY, BorderLayout.CENTER);

                FRAME.pack();

                loadingFrame.dispose();

                FRAME.setVisible(true);
                FRAME.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
//                }

                return 42;
            }

        }
                .execute();

    }
}
