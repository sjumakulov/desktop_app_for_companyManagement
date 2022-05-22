package popups;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static yutong.tools.webcam.flipBufferedImage;
import static yutong.tools.webcam.webCam;

public class webCamPopup {

    public static void showWebCamPopup(JFrame FRAME, Webcam cam, JTextField tf) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        wenCamDialogPopup(FRAME, cam, tf);
        glassPane.setVisible(false);
    }

    public static JDialog wenCamDialogPopup(JFrame FRAME, Webcam cam, JTextField tf) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setMinimumSize(new Dimension(640, 577));
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);

        WebcamPanel p = webCam(cam);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(p, BorderLayout.CENTER);

        jd.getContentPane().add(button("Ўчириш", "no", p, panel, tf, jd), BorderLayout.PAGE_START);
        jd.getContentPane().add(panel, BorderLayout.CENTER);
        jd.getContentPane().add(button("Олиш", "yes", p, panel, tf, jd), BorderLayout.PAGE_END);
        jd.setResizable(false);

        jd.setVisible(true);
        p.getWebcam().close();

        return jd;
    }

    private static JPanel button(String label, String name, WebcamPanel p, JPanel panel, JTextField tf, JDialog jd) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 16));

        JPanel bcon = new JPanel(new BorderLayout());
        bcon.setPreferredSize(new Dimension(135, 35));
        bcon.add(button);

        if (name.equals("no")) {
            button.setBackground(Color.decode("#DB4437"));
            button.setForeground(Color.white);
            button.setEnabled(false);
        } else {
            button.setBackground(Color.white);
            button.setForeground(Color.decode("#4285F4"));
            bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
            button.setName("capture");
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (name.equals("no")) {
                    button.setBackground(Color.decode("#df5549"));

                } else {
                    button.setBackground(Color.decode("#f7faff"));
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#d6e5ff")));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (name.equals("no")) {
                    button.setBackground(Color.decode("#DB4437"));

                } else {
                    button.setBackground(Color.white);
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (name.equals("no")) {
                    JButton captureButton = (JButton) ((JPanel) jd.getContentPane().getComponent(2)).getComponent(0);
                    captureButton.setText("Олиш");
                    captureButton.setName("capture");

                    button.setEnabled(false);

                    panel.remove(0);
                    panel.add(p, BorderLayout.CENTER);
                    panel.revalidate();

                } else {
                    if (button.getName().equals("capture")) {
                        JButton deleteButton = (JButton) ((JPanel) jd.getContentPane().getComponent(0)).getComponent(0);
                        deleteButton.setEnabled(true);

                        button.setName("ready");
                        button.setText("Сақлаш");

                        BufferedImage img = flipBufferedImage(p.getImage());
                        panel.remove(0);
                        panel.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
                        panel.revalidate();
                        panel.repaint();
                    } else {
                        jd.dispose();
                        tf.setText("webcamImage.jpg");
                    }

                }
            }
        });

        return bcon;
    }
}
