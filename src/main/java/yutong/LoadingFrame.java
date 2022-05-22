package yutong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static yutong.tools.MyImage.frontPageImage;

public class LoadingFrame extends JFrame {

    private final ImageIcon icon = new ImageIcon(new ImageIcon("./resources/icon.png").getImage().getScaledInstance(40, -1, Image.SCALE_SMOOTH));

    JLabel contactInfo = new JLabel("Telegram: @sjumakulov | Instagram: @sjumakuloff");

    public LoadingFrame() {
        contactInfo.setForeground(Color.white);
        contactInfo.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        contactInfo.setBackground(Color.decode("#ff7700"));
        contactInfo.setOpaque(true);
        contactInfo.setFont(new Font("", Font.BOLD, 12));

        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 250));
        setUndecorated(true);
        setIconImage(icon.getImage());

        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(Color.decode("#f5f5f5"));
        getContentPane().add(frontPageImage(365, 205), BorderLayout.CENTER);
        getContentPane().add(contactInfo, BorderLayout.PAGE_END);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
