package yutong.tools;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyImage {

    public static JLabel frontPageImage(int w, int h) {
        ImageIcon img = new ImageIcon("./resources/image.png");
        img = new ImageIcon(img.getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH));

        return new JLabel(img);
    }

}
