package yutong.tools;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class OpenFile {

    public static void openFile(String path) {
        try {

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(path));
            }
        } catch (IOException ioe) {
            System.out.println("Error while opening file");
        }
    }

}
