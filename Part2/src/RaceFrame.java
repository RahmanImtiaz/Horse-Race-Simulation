import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A frame that displays the gui race of 1 to 3 horses. The frame contains a
 * panel for each track and a horse on it.
 * The frame also contains a text area for each horse, displaying the horse's
 * name and confidence level.
 * 
 * @author Rahman Imtiaz
 * @version 1.0
 */

public class RaceFrame extends JFrame {
    private Color trackColour;
    private int raceLength;
    private int horseNum;
    private List<Horse> horses;
    private JTextArea horseDetails;
    private List<JTextArea> horseDetailsList = new ArrayList<>();
    private int horseWidth;
    private List<JLabel> betsLabelList;
    private int selectedHorseIndex; // bet on horse 0, 1, 2
    private JButton placeBetBtn;

    public RaceFrame(Color trackColour, int raceLength, int horseNum, List<Horse> horses, User user) {
        this.trackColour = trackColour;
        this.raceLength = raceLength;
        this.horseNum = horseNum;
        this.horses = horses;
        this.selectedHorseIndex = 0;

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
                    g.fillRect(50, 50, ((raceLength) * 10), 60);

                    g.setColor(Color.WHITE); // Set the color of the border
                    g.drawRect(50, 50, ((raceLength) * 10), 60);
                }
            };
            horsePanel.setLayout(null); // Set layout to null
            horsePanel.setPreferredSize(new Dimension(((raceLength) * 10), 70)); // Set preferred size to 70

            JLabel horseGUI = null;
            horseGUI = horses.get(i).getHorseGUI();

            if (horseGUI != null) {
                horseGUI.setBounds(50, 50, horseGUI.getPreferredSize().width, horseGUI.getPreferredSize().height);
                horseWidth = horseGUI.getPreferredSize().width;
                horsePanel.add(horseGUI);
            }

            horseDetails = new JTextArea();
            horseDetails.setText(
                    " " +  horses.get(i).getName() + "\n(Current confidence "
                            +  horses.get(i).getConfidence() + ")");
                            
            horseDetails.setBounds(((raceLength) * 10) + 60, 50, 200, 30);
            horseDetails.setEditable(false);
            horseDetails.setForeground(Color.WHITE);
            horseDetails.setBackground(Color.BLACK);
            horsePanel.add(horseDetails);
            horseDetailsList.add(horseDetails);

            raceDesignPanel.add(horsePanel);
        }

        this.add(raceDesignPanel);

        setBettingPanel(user);

        this.setVisible(true);
    }

    public void updateHorseDetails(int i) {
        JTextArea horseDetails = horseDetailsList.get(i);
        horseDetails.setText(
            " " +  horses.get(i).getName() + "\n(Current confidence "
                    +  horses.get(i).getConfidence() + ")");
    }

    public int getHorseWidth() {
        return horseWidth;
    }

    public void setBettingPanel(User user) {

        betsLabelList = new ArrayList<>();
        user.setBetAmount(0);
        user.setHorseSelected(-1);

        JPanel bettingPanel = new JPanel();
        bettingPanel.setLayout(new BoxLayout(bettingPanel, BoxLayout.Y_AXIS));

        bettingPanel.setBackground(Color.DARK_GRAY);
        bettingPanel.setPreferredSize(new Dimension(400, bettingPanel.getHeight()));

        JLabel bettingLabel = new JLabel("Place your bets here!");
        bettingLabel.setForeground(Color.WHITE);
        bettingPanel.add(createCenteredBox(bettingLabel));

        List<String> horseNamesList = new ArrayList<>();

        int horsenum = 1;
        for (Horse horse : horses) {
            horseNamesList.add("Horse "+horsenum+": " + horse.getName());
            horsenum++;
        }

        String[] horseNames = horseNamesList.toArray(new String[0]);
        JComboBox<String> comboBox = new JComboBox<>(horseNames);
        comboBox.setMaximumSize(new Dimension(100, 30));
        bettingPanel.add(createCenteredBox(comboBox));

        JLabel oddsLabel = new JLabel("");
        oddsLabel.setForeground(Color.WHITE);
        bettingPanel.add(createCenteredBox(oddsLabel));

        double odds = horses.get(0).calculateOdds(raceLength);
        oddsLabel.setText(String.format("Odds: %.2f", odds));
        oddsLabel.setForeground(Color.WHITE);

        betsLabelList.add(oddsLabel);

        // Refresh the panel
        bettingPanel.revalidate();
        bettingPanel.repaint();

        // Add an ActionListener to the JComboBox
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected item
                selectedHorseIndex = comboBox.getSelectedIndex();

                // Find the Horse object based on the selected index
                double odds = horses.get(selectedHorseIndex).calculateOdds(raceLength);

                // Display the odds (for example, in a JLabel)
                oddsLabel.setText(String.format("Odds: %.2f", odds));
                oddsLabel.setForeground(Color.WHITE);

                // Refresh the panel
                bettingPanel.revalidate();
                bettingPanel.repaint();
            }
        });

        JLabel betAmountLabel = new JLabel("Bet Amount: " + user.getBetAmount());
        betAmountLabel.setForeground(Color.WHITE);
        JTextField betAmountField = new JTextField(10);
        betAmountField.setMaximumSize(new Dimension(100, 30));
        betAmountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        bettingPanel.add(createCenteredBox(betAmountField));
        bettingPanel.add(createCenteredBox(betAmountLabel));

        betsLabelList.add(betAmountLabel);

        JLabel balanceLabel = new JLabel("Balance: £" + user.getBalance());
        balanceLabel.setForeground(Color.WHITE);
        bettingPanel.add(createCenteredBox(balanceLabel));

        betsLabelList.add(balanceLabel);

        placeBetBtn = new JButton("Place Bet");

        placeBetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the JTextField
                String betAmountText = betAmountField.getText();

                // Parse the text as an integer
                try {
                    int betAmount = Integer.parseInt(betAmountText);

                    if (betAmount <= user.getBalance()) {
                        user.setBetAmount(betAmount);
                        user.setHorseSelected(comboBox.getSelectedIndex());

                        betAmountField.setText("");
                        betAmountLabel.setText("Bet Amount: " + user.getBetAmount());
                        placeBetBtn.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "You do not have enough balance to place this bet.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number for the bet amount.");
                }
            }
        });

        bettingPanel.add(createCenteredBox(placeBetBtn));

        this.add(bettingPanel, BorderLayout.EAST);
    }

    private Box createCenteredBox(JComponent component) {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(component);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    public void updateStatDetails(User user) {
        double odds = horses.get(selectedHorseIndex).calculateOdds(raceLength);

        // update the odds label
        betsLabelList.get(0).setText(String.format("Odds: %.2f", odds));

        // update the bet amount label
        betsLabelList.get(1).setText("Bet Amount: " + user.getBetAmount());

        // update the balance label
        betsLabelList.get(2).setText("Balance: £" + user.getBalance());
    }

    public void enableBetBtn() {
        placeBetBtn.setEnabled(true);
    }

    public void disableBetBtn() {
        placeBetBtn.setEnabled(false);
    }
}
