import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HorseIcon implements Icon {
    private final int size = 60; // Increase size for better detail
    private final Color color;
    private final Color overlapColor; // Color for the overlapping part
    private String unicode;

    public HorseIcon(Color color, Color overlapColor, String unicode) {
        this.color = color;
        this.overlapColor = overlapColor;
        this.unicode = unicode;
    }

    @Override
public void paintIcon(Component c, Graphics g, int x, int y) {

    // Create a BufferedImage to draw the horse emoji
    BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();

    // Set font size and color for drawing the emoji
    g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size));
    g2d.setColor(color); // Set the desired color here

    // Draw horse emoji onto the BufferedImage
    g2d.drawString(unicode, 0, size);

    // Flip the BufferedImage horizontally
    BufferedImage mirroredImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D mirroredG2d = mirroredImg.createGraphics();
    mirroredG2d.drawImage(img, 0, 0, size, size, size, 0, 0, size, null);
    mirroredG2d.dispose();

    // Iterate over each pixel
    for (int i = 0; i < mirroredImg.getHeight(); i++) {
        for (int j = 0; j < mirroredImg.getWidth(); j++) {
            // Get the pixel's color
            Color color = new Color(mirroredImg.getRGB(j, i));

            // Check if the color is green (this checks if green is the dominant component)
            if (color.getGreen() > color.getRed() && color.getGreen() > color.getBlue()) {
                // Calculate the relative luminance
                double luminance = 0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue();

                // Calculate the equivalent shade of the overlap color
                double overlapLuminance = 0.2126 * overlapColor.getRed() + 0.7152 * overlapColor.getGreen()
                        + 0.0722 * overlapColor.getBlue();
                double ratio = luminance / overlapLuminance;

                int newRed = Math.min(255, (int) (overlapColor.getRed() * ratio));
                int newGreen = Math.min(255, (int) (overlapColor.getGreen() * ratio));
                int newBlue = Math.min(255, (int) (overlapColor.getBlue() * ratio));

                Color alteredColor = new Color(newRed, newGreen, newBlue);

                // Set the pixel to the new color
                mirroredImg.setRGB(j, i, alteredColor.getRGB());
            }
        }
    }

    // Draw the mirrored image onto the component's graphics
    g.drawImage(mirroredImg, x, y, null);
}
    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}
