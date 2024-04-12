import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RaceFrame extends JFrame {
    private Color trackColour;
    private int raceLength;
    private int horseNum;
    private Horse h1, h2, h3;

    public RaceFrame(Color trackColour, int raceLength, int horseNum, Horse h1, Horse h2, Horse h3) {
        this.trackColour = trackColour;
        this.raceLength = raceLength;
        this.horseNum = horseNum;
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;

        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel raceDesignPanel = new JPanel();
        raceDesignPanel.setLayout(new BoxLayout(raceDesignPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < horseNum; i++) {
            JPanel horsePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    setBackground(Color.BLACK);
                    g.setColor(trackColour);
                    g.fillRect(50, 50, ((raceLength)*10), 60);
                }
            };
            horsePanel.setLayout(null); // Set layout to null
            horsePanel.setPreferredSize(new Dimension(((raceLength)*10), 70)); // Set preferred size to 70
        
            JLabel horseGUI = null;
            if (i == 0) {
                horseGUI = h1.getHorseGUI();
            } else if (i == 1) {
                horseGUI = h2.getHorseGUI();
            } else if (i == 2) {
                horseGUI = h3.getHorseGUI();
            }
        
            if (horseGUI != null) {
                horseGUI.setBounds(50, 50, horseGUI.getPreferredSize().width, horseGUI.getPreferredSize().height);
                horsePanel.add(horseGUI);
            }
        
            raceDesignPanel.add(horsePanel);
        }

        this.add(raceDesignPanel);
        this.setVisible(true);

        
    }
}