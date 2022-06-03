import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class JImageDisplay extends javax.swing.JComponent {
    java.awt.image.BufferedImage image;

    public JImageDisplay(int wight, int height) {
        this.image = new BufferedImage(wight, height, BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(wight, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    public void clearImage() {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                image.setRGB(i, j, 0);
            }
        }
    }

    public void drawPixel(int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }
}