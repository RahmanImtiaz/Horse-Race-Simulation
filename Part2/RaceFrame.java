import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

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
    private Color sidepanelColour = Color.decode("#191a1f");
    private Color horseListColour = Color.decode("#2b2d38");
    private JPanel horsePanel;
    private JPanel bettingPanel;
    private JPanel horseListPanel;
    private JComboBox<String> BettingcomboBox;

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

            horsePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    setBackground(Color.BLACK);
                    g.setColor(trackColour);
                    g.fillRect(50, 0, ((raceLength) * 10), 60);

                    g.setColor(Color.WHITE); // Set the color of the border
                    g.drawRect(50, 0, ((raceLength) * 10), 60);
                }
            };
            horsePanel.setLayout(null); // Set layout to null
            horsePanel.setSize(horsePanel.getPreferredSize().width, 120);

            if (i < horses.size()) {
                JLabel horseGUI = null;
                horseGUI = horses.get(i).getHorseGUI();

                if (horseGUI != null) {
                    horseGUI.setBounds(50, 0, horseGUI.getPreferredSize().width, horseGUI.getPreferredSize().height);
                    horseWidth = horseGUI.getPreferredSize().width;
                    horsePanel.add(horseGUI);
                }

                horseDetails = new JTextArea();
                horseDetails.setText(
                        " " + horses.get(i).getName() + "\n(Current confidence "
                                + horses.get(i).getConfidence() + ")");
                horseDetails.setBounds(((raceLength) * 10) + 60, 1, 200, 30);
                horseDetails.setEditable(false);
                horseDetails.setForeground(Color.WHITE);
                horseDetails.setBackground(Color.BLACK);
                horsePanel.add(horseDetails);
                horseDetailsList.add(horseDetails);

                JLabel horseBreedIcon = new JLabel(horses.get(i).getBreedIcon());
                horseBreedIcon.setBounds(((raceLength) * 10) + 60, 31, horseBreedIcon.getPreferredSize().width,
                        horseBreedIcon.getPreferredSize().height);
                horsePanel.add(horseBreedIcon);
            }

            raceDesignPanel.add(horsePanel);
        }

        this.add(raceDesignPanel);

        setBettingPanel(user);

        this.setVisible(true);
    }

    public void updateHorseDetails(int i) {
        JTextArea horseDetails = horseDetailsList.get(i);
        horseDetails.setText(
                " " + horses.get(i).getName() + "\n(Current confidence "
                        + horses.get(i).getConfidence() + ")");
    }

    public int getHorseWidth() {
        return horseWidth;
    }

    public void setBettingPanel(User user) {

        betsLabelList = new ArrayList<>();
        user.setBetAmount(0);
        user.setHorseSelected(-1);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());

        sidePanel.setBackground(sidepanelColour);
        sidePanel.setPreferredSize(new Dimension(300, sidePanel.getHeight()));

        bettingPanel = new JPanel();
        bettingPanel.setLayout(new BoxLayout(bettingPanel, BoxLayout.Y_AXIS));

        bettingPanel.setBackground(sidepanelColour);
        bettingPanel.setPreferredSize(new Dimension(300, 200));

        JLabel bettingLabel = new JLabel("Place your bets here!");
        bettingLabel.setForeground(Color.WHITE);
        bettingPanel.add(createCenteredBox(bettingLabel));

        List<String> horseNamesList = new ArrayList<>();

        int horsenum = 1;
        for (Horse horse : horses) {
            horseNamesList.add("Horse " + horsenum + ": " + horse.getName());
            horsenum++;
        }

        String[] horseNames = horseNamesList.toArray(new String[0]);
        BettingcomboBox = new JComboBox<>(horseNames);
        BettingcomboBox.setMaximumSize(new Dimension(100, 30));
        bettingPanel.add(createCenteredBox(BettingcomboBox));

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
        BettingcomboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected item
                selectedHorseIndex = BettingcomboBox.getSelectedIndex();

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
                        user.setHorseSelected(BettingcomboBox.getSelectedIndex());

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

        sidePanel.add(bettingPanel, BorderLayout.NORTH);
        sidePanel.add(horseListPanel(), BorderLayout.CENTER);

        this.add(sidePanel, BorderLayout.EAST);
    }

    public JPanel horseListPanel() {
        horseListPanel = new JPanel();
        horseListPanel.setLayout(new BoxLayout(horseListPanel, BoxLayout.Y_AXIS));
        horseListPanel.setPreferredSize(new Dimension(300, horseListPanel.getHeight()));
        horseListPanel.setBorder(new MatteBorder(10, 10, 10, 10, sidepanelColour));
        horseListPanel.setBackground(horseListColour);

        for (Horse horse : horses) {
            JButton horseButton = new JButton(horse.getName());
            horseButton.setBackground(bettingPanel.getBackground());
            horseButton.setPreferredSize(new Dimension(600, 30));
            horseButton.setIcon(horse.getBreedIcon());
            horseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a new JDialog
                    JDialog dialog = new JDialog();
                    dialog.setLayout(new GridLayout(3, 2));

                    // Create JTextFields for the horse's name and confidence
                    JTextField nameField = new JTextField(horse.getName());
                    JTextField confidenceField = new JTextField(String.valueOf(horse.getConfidence()));

                    confidenceField.setEditable(false);

                    // Create JButtons for saving and canceling
                    JButton saveButton = new JButton("Save");
                    saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Update the horse's name and confidence
                            horse.setName(nameField.getText());
                            horse.setConfidence(Double.parseDouble(confidenceField.getText()));

                            // Update the horse's details
                            updateHorseDetails(horses.indexOf(horse));
                            horseButton.setText(horse.getName());

                            for (int i = 0; i < BettingcomboBox.getItemCount(); i++) {
                                // Get the current item
                                String currentItem = BettingcomboBox.getItemAt(i);
                            
                                // Modify the current item
                                int count = i + 1;
                                String newItem = "Horse " + count + ": " + horses.get(i).getName();
                            
                                // Insert the new item at the current index
                                BettingcomboBox.insertItemAt(newItem, i);
                            
                                // Remove the old item
                                BettingcomboBox.removeItemAt(i + 1);
                            }


                            // Close the dialog
                            dialog.dispose();
                        }
                    });

                    JButton cancelButton = new JButton("Cancel");
                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Close the dialog without saving
                            dialog.dispose();
                        }
                    });

                    // Add the JTextFields and JButtons to the dialog
                    dialog.add(new JLabel("Name:"));
                    dialog.add(nameField);
                    dialog.add(new JLabel("Confidence:"));
                    dialog.add(confidenceField);
                    dialog.add(saveButton);
                    dialog.add(cancelButton);

                    // Show the dialog
                    dialog.pack();
                    dialog.setVisible(true);
                }
            });
            horseListPanel.add(createCenteredBox(horseButton));
        }

        return horseListPanel;
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
