import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class HorseIcon implements Icon {
    private final int size = 16;
    private final Color color;

    public HorseIcon(Color color) {
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, size, size);
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