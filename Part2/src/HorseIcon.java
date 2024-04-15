import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HorseIcon implements Icon {
    private final int size = 60; // Increase size for better detail
    private final Color color;
    private final Color overlapColor; // Color for the overlapping part

    public HorseIcon(Color color, Color overlapColor) {
        this.color = color;
        this.overlapColor = overlapColor;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        // Draw a rectangle with the overlap color to cover the overlapping part
        g.setColor(overlapColor);
        g.fillRect(x, y, size, size);

        // Create a BufferedImage to draw the horse emoji
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Set font size and color for drawing the emoji
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size));
        g2d.setColor(color); // Set the desired color here

        // Draw horse emoji onto the BufferedImage
        g2d.drawString("\uD83D\uDC0E", 0, size);

        // Flip the BufferedImage horizontally
        BufferedImage mirroredImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D mirroredG2d = mirroredImg.createGraphics();
        mirroredG2d.drawImage(img, size, 0, 0, size, 0, 0, size, size, null);
        mirroredG2d.dispose();

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
