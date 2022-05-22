package yutong;

import javax.swing.SwingUtilities;
import static yutong.MainFrame.MainFrame;

public class Yutong {

    public static void main(String[] args) {

        Runnable GUITask = () -> {
            window();
        };
        SwingUtilities.invokeLater(GUITask);
    }

    public static void window() {
        LoadingFrame loadingFrame = new LoadingFrame();
        MainFrame(loadingFrame);
    }
}
