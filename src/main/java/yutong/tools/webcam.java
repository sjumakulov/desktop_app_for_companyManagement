package yutong.tools;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class webcam {

    public static WebcamPanel webCam(Webcam cam) {

        cam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel p = new WebcamPanel(cam);

        p.setImageSizeDisplayed(true);
        p.setMirrored(true);

        return p;
    }

    public static BufferedImage flipBufferedImage(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                flipped.setRGB((w - 1) - x, y, img.getRGB(x, y));
            }
        }
        File outputfile = new File("webcamImage.jpg");
        try {
            ImageIO.write(flipped, "jpg", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(webcam.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flipped;
    }

}
