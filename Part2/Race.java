import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.lang.reflect.Array;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Rahman Imtiaz
 * @version 1.0
 */

public class Race {
    private int raceLength;
    private Color trackColour;
    private int horseNum;
    private List<Horse> horses;
    private int laneAmount;
    private JPanel panel;
    private JPanel horseDesignPanel;
    private JFrame frame;
    private Timer timer;
    private JFrame statsFrame;
    private RaceFrame raceFrame;
    private User user;
    private int currentHorseCount = 1;
    private ArrayList<JTextField> nameTextFields;
    private ArrayList<JTextField> confidenceTextFields;
    private ArrayList<Object[]> breedItems;
    private ArrayList<String> accessoriesList;
    private JButton addHorsebtn;
    private JComboBox breedsMenu;
    private JComboBox accessoriesMenu;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) {
        // initialise instance variables
        raceLength = distance;
        horses = new ArrayList<Horse>();
        user = new User(1000);
    }

    private boolean allHorsesHaveFallen() {
        for (Horse horse : horses) {
            if (horse != null && !horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }

    private void changeHorseConfidence(Horse horse) {

        if (horse != null) {
            if (raceWonBy(horse)) {
                horse.setConfidence(horse.getConfidence() + 0.1);
            } else if (horse.hasFallen()) {
                horse.setConfidence(horse.getConfidence() - 0.1);
            }
        }
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    public void moveHorse(Horse theHorse) {
        // if the horse has fallen it cannot move,
        // so only run if it has not fallen

        if (!theHorse.hasFallen()) {
            // the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
                changeHorseConfidence(theHorse);
            }

            // the probability that the horse will fall is very small (max is 0.1)
            // but will also will depends exponentially on confidence
            // so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
                theHorse.horseFallenGUI();
                changeHorseConfidence(theHorse);
            }

            // if the horse has not moved forward at all, then it has fallen
            if (theHorse.getConfidence() == 0) {
                theHorse.fall();
                theHorse.horseFallenGUI();
            }
        }
    }

    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {
        if (theHorse != null && theHorse.getDistanceTravelled() == ((raceLength * 10) - raceFrame.getHorseWidth())) {
            return true;
        } else {
            return false;
        }
    }

    public void startRaceGUI() {
        frame = new JFrame("HorseRaceSimulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new GridBagLayout()); // Set layout of frame to GridBagLayout

        // Create a panel with BoxLayout
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.black);

        // Create title
        JLabel title = new JLabel("Horse Race Simulator");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.white);

        // Create buttons
        JButton button1 = new JButton("Start: Design Track");
        JButton button2 = new JButton("Exit");

        button1.setMaximumSize(new Dimension(200, 50));
        button2.setMaximumSize(new Dimension(200, 50));

        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button1.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add action listeners to buttons
        button1.addActionListener(e -> designTrack());
        button2.addActionListener(e -> System.exit(0));

        // Add components to panel
        panel.add(title);
        panel.add(button1);
        panel.add(button2);

        // Add panel to frame
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER; // Center panel in frame
        frame.add(panel, gbc);

        frame.setVisible(true);
    }

    private void designTrack() {
        // Remove all components from the panel
        panel.removeAll();

        // Create a new panel
        JPanel trackDesignPanel = new JPanel();
        trackDesignPanel.setLayout(new BoxLayout(trackDesignPanel, BoxLayout.Y_AXIS));
        trackDesignPanel.setBackground(Color.black);
        trackDesignPanel.setBorder(BorderFactory.createLineBorder(Color.white, 5, true));

        // Add components to the new panel
        JLabel title = new JLabel("Design Track");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.white);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(title);

        JLabel label1 = new JLabel("Enter the length of the track (in metres) (10-100):");
        label1.setForeground(Color.white);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(label1);

        JTextField raceLengthTextField = new JTextField();
        raceLengthTextField.setMaximumSize(new Dimension(100, 30));
        raceLengthTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(raceLengthTextField);

        JLabel label3 = new JLabel("How many lanes would you like? (2-10)");
        label3.setForeground(Color.white);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(label3);

        JTextField numTracksTextField = new JTextField();
        numTracksTextField.setMaximumSize(new Dimension(100, 30));
        numTracksTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(numTracksTextField);

        JLabel label2 = new JLabel("What colour would you like the track to be?");
        label2.setForeground(Color.white);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(label2);

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeBackgroundColor(colorChooser, Color.BLACK);
        changeTextColor(colorChooser, Color.white);
        trackDesignPanel.add(colorChooser);

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> {

            try {
                int tempRaceLength = Integer.parseInt(raceLengthTextField.getText());
                int tempHorseNum = Integer.parseInt(numTracksTextField.getText());

                if (tempRaceLength >= 10 && tempRaceLength <= 100 && tempHorseNum > 1 && tempHorseNum <= 10) {
                    raceLength = tempRaceLength;
                    horseNum = tempHorseNum;
                    trackColour = colorChooser.getColor();
                    customiseHorses();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input. Please enter a positive race length between 10 and 100 and a horse number between 2 and 10.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            }
        });
        trackDesignPanel.add(submit);

        // Add the new panel to the original panel
        panel.add(trackDesignPanel);

        // Refresh the panel
        panel.revalidate();
        panel.repaint();
    }

    private void customiseHorses() {
        // Remove all components from the panel
        panel.removeAll();

        // Create a new panel
        horseDesignPanel = new JPanel();
        horseDesignPanel.setLayout(new BoxLayout(horseDesignPanel, BoxLayout.Y_AXIS));
        horseDesignPanel.setBackground(Color.black);
        horseDesignPanel.setBorder(BorderFactory.createLineBorder(Color.white, 5, true));
        horseDesignPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        horseDesignPanel.setMaximumSize(new Dimension(500, 500));

        // Create arrays to store the text fields
        nameTextFields = new ArrayList<>();
        confidenceTextFields = new ArrayList<>();
        breedItems = new ArrayList<>();
        accessoriesList = new ArrayList<>();

        addHorseForm(nameTextFields, confidenceTextFields, breedItems, accessoriesList);

        JButton submit = new JButton("Submit & Start Race");
        submit.setMaximumSize(new Dimension(200, 50));
        submit.setEnabled(false); // Disable the button initially, so that minimum 2 horses are added
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> {

            try {

                Object[] newSelectedItem = (Object[]) breedsMenu.getSelectedItem();
                String breedname = (String) newSelectedItem[1];
                if ("Select a Horse".equals(breedname)) {
                    JOptionPane.showMessageDialog(null, "Please select a horse.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    breedItems.add(newSelectedItem);
                }

                String name = nameTextFields.get(currentHorseCount - 2).getText();
                double confidence = Double.parseDouble(confidenceTextFields.get(currentHorseCount - 2).getText());
                ImageIcon icon = null;
                String breed = null;
                if ((currentHorseCount - 2) < breedItems.size()) {
                    Object[] selectedItem = breedItems.get(currentHorseCount - 2);
                    icon = (ImageIcon) selectedItem[0];
                    breed = (String) selectedItem[1];
                }

                String selectedaccessories = (String) accessoriesMenu.getSelectedItem();
                if ("Select an Accessory".equals(selectedaccessories)) {
                    JOptionPane.showMessageDialog(null, "Please select an accessory.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    accessoriesList.add(selectedaccessories);
                }

                String accessories = null;
                if ((currentHorseCount - 2) < accessoriesList.size()) {
                    accessories = accessoriesList.get(currentHorseCount - 2);
                }

                if (confidence >= 0 && confidence <= 1 && icon != null && accessories != null) {

                    horses.get(currentHorseCount - 2).setName(name);
                    horses.get(currentHorseCount - 2).setConfidence(confidence);
                    horses.get(currentHorseCount - 2).setBreed(breed);
                    horses.get(currentHorseCount - 2).setBreedIcon(icon);
                    horses.get(currentHorseCount - 2).setAccessory(accessories);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input! Please enter a number between 0 and 1. And/or select a breed/Accessory.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                return;
            }

            startGUI();
        });

        JButton newHorseBtn = new JButton("Submit & add Another Horse");
        newHorseBtn.setMaximumSize(new Dimension(200, 50));
        if (horses.size() < horseNum) {
            newHorseBtn.setEnabled(true);
        } else {
            newHorseBtn.setEnabled(false);
        }
        newHorseBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newHorseBtn.addActionListener(e -> {

            try {

                Object[] newSelectedItem = (Object[]) breedsMenu.getSelectedItem();
                String breedname = (String) newSelectedItem[1];
                if ("Select a Horse".equals(breedname)) {
                    JOptionPane.showMessageDialog(null, "Please select a horse.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    breedItems.add(newSelectedItem);
                }

                String name = nameTextFields.get(currentHorseCount - 2).getText();
                double confidence = Double.parseDouble(confidenceTextFields.get(currentHorseCount - 2).getText());
                ImageIcon icon = null;
                String breed = null;
                if ((currentHorseCount - 2) < breedItems.size()) {
                    Object[] selectedItem = breedItems.get(currentHorseCount - 2);
                    icon = (ImageIcon) selectedItem[0];
                    breed = (String) selectedItem[1];
                }

                String selectedaccessories = (String) accessoriesMenu.getSelectedItem();
                if ("Select an Accessory".equals(selectedaccessories)) {
                    JOptionPane.showMessageDialog(null, "Please select an accessory.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    accessoriesList.add(selectedaccessories);
                }

                String accessories = null;
                if ((currentHorseCount - 2) < accessoriesList.size()) {
                    accessories = accessoriesList.get(currentHorseCount - 2);
                }

                if (confidence >= 0 && confidence <= 1 && icon != null && accessories != null) {

                    horses.get(currentHorseCount - 2).setName(name);
                    horses.get(currentHorseCount - 2).setConfidence(confidence);
                    horses.get(currentHorseCount - 2).setBreed(breed);
                    horses.get(currentHorseCount - 2).setBreedIcon(icon);
                    horses.get(currentHorseCount - 2).setAccessory(accessories);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input! Please enter a number between 0 and 1. And/or select a breed/Accessory.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                return;
            }

            horseDesignPanel.removeAll();
            addHorseForm(nameTextFields, confidenceTextFields, breedItems, accessoriesList);

            horseDesignPanel.add(submit);
            horseDesignPanel.add(newHorseBtn);

            if (horses.size() < horseNum) {
                newHorseBtn.setEnabled(true);
                submit.setEnabled(true);
            } else {
                newHorseBtn.setEnabled(false);
                submit.setEnabled(true);
            }

            // Refresh the panel
            panel.revalidate();
            panel.repaint();

        });

        horseDesignPanel.add(submit);
        horseDesignPanel.add(newHorseBtn);

        // Add the new panel to the original panel
        panel.add(horseDesignPanel);

        // Refresh the panel
        panel.revalidate();
        panel.repaint();

    }

    private void addHorseWindow() {
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new GridBagLayout()); // Set layout of frame to GridBagLayout
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER; // Center panel in frame

        // Create a new panel
        horseDesignPanel.removeAll();

        addHorseForm(nameTextFields, confidenceTextFields, breedItems, accessoriesList);

        JButton submit = new JButton("Add Horse");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> {

            try {

                Object[] newSelectedItem = (Object[]) breedsMenu.getSelectedItem();
                String breedname = (String) newSelectedItem[1];
                if ("Select a Horse".equals(breedname)) {
                    JOptionPane.showMessageDialog(null, "Please select a horse.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    breedItems.add(newSelectedItem);
                }

                String name = nameTextFields.get(currentHorseCount - 2).getText();
                double confidence = Double.parseDouble(confidenceTextFields.get(currentHorseCount - 2).getText());
                ImageIcon icon = null;
                String breed = null;
                if ((currentHorseCount - 2) < breedItems.size()) {
                    Object[] selectedItem = breedItems.get(currentHorseCount - 2);
                    icon = (ImageIcon) selectedItem[0];
                    breed = (String) selectedItem[1];
                }

                String selectedaccessories = (String) accessoriesMenu.getSelectedItem();
                if ("Select an Accessory".equals(selectedaccessories)) {
                    JOptionPane.showMessageDialog(null, "Please select an accessory.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    accessoriesList.add(selectedaccessories);
                }

                String accessories = null;
                if ((currentHorseCount - 2) < accessoriesList.size()) {
                    accessories = accessoriesList.get(currentHorseCount - 2);
                }

                if (confidence >= 0 && confidence <= 1 && icon != null && accessories != null) {

                    horses.get(currentHorseCount - 2).setName(name);
                    horses.get(currentHorseCount - 2).setConfidence(confidence);
                    horses.get(currentHorseCount - 2).setBreed(breed);
                    horses.get(currentHorseCount - 2).setBreedIcon(icon);
                    horses.get(currentHorseCount - 2).setAccessory(accessories);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input! Please enter a number between 0 and 1. And/or select a breed/Accessory.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                return;
            }
            raceFrame.dispose(); // Close the current
            if (statsFrame != null) {
                statsFrame.dispose(); // Close the stats frame
            }
            startGUI();
        });

        horseDesignPanel.add(submit);

        frame.add(panel, gbc);

        frame.setVisible(true);

        // Refresh the panel
        horseDesignPanel.revalidate();
        horseDesignPanel.repaint();

    }

    @SuppressWarnings("unchecked")
    private void addHorseForm(ArrayList<JTextField> nameTextFields, ArrayList<JTextField> confidenceTextFields,
            ArrayList<Object[]> breedItems, ArrayList<String> accessoriesList) {

        horses.add(new Horse());

        JLabel Colourlabel = new JLabel("What colour would you like horse " + currentHorseCount + " to be?");
        Colourlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Colourlabel.setForeground(Color.white);
        horseDesignPanel.add(Colourlabel);

        JButton button = new JButton();
        button.setMaximumSize(new Dimension(100, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        final int finalI = currentHorseCount; // Create a final copy of i

        // Default horse colour is white
        JLabel horseLabel = new JLabel(new HorseIcon(trackColour, Color.white, "\uD83D\uDC0E"));
        horses.get(currentHorseCount - 1).setHorseGUI(horseLabel);
        horses.get(finalI - 1).setHorseColor(Color.white);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color initialBackground = button.getBackground();
                Color color = JColorChooser.showDialog(null, "JColorChooser Sample", initialBackground);

                button.setBackground(color);

                JLabel horseLabel = new JLabel(new HorseIcon(trackColour, color, "\uD83D\uDC0E"));
                horses.get(finalI - 1).setHorseGUI(horseLabel);
                horses.get(finalI - 1).setHorseColor(color);

            }
        });

        horseDesignPanel.add(button);

        JLabel label = new JLabel("Enter the name of horse " + currentHorseCount + ":");
        label.setForeground(Color.white);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.add(label);

        JTextField nameTextField = new JTextField();
        nameTextField.setMaximumSize(new Dimension(100, 30));
        nameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.add(nameTextField);

        JLabel label2 = new JLabel("Enter the confidence of horse " + currentHorseCount + " (0-1):");
        label2.setForeground(Color.white);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.add(label2);

        JTextField confidenceTextField = new JTextField();
        confidenceTextField.setMaximumSize(new Dimension(100, 30));
        confidenceTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.add(confidenceTextField);

        JLabel label3 = new JLabel("What breed is horse " + currentHorseCount + "?");
        label3.setForeground(Color.white);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.add(label3);

        String currentDirectory = System.getProperty("user.dir");
        // Create an array to hold the icons
        ImageIcon[] icons = new ImageIcon[5];
        icons[0] = null;
        icons[1] = new ImageIcon(
                new ImageIcon(currentDirectory + "/horseIcon.png").getImage().getScaledInstance(25, 25,
                        Image.SCALE_SMOOTH));
        icons[2] = new ImageIcon(
                new ImageIcon(currentDirectory + "/ponyIcon.png").getImage().getScaledInstance(25, 25,
                        Image.SCALE_SMOOTH));
        icons[3] = new ImageIcon(
                new ImageIcon(currentDirectory + "/donkeyIcon.png").getImage().getScaledInstance(25, 25,
                        Image.SCALE_SMOOTH));
        icons[4] = new ImageIcon(
                new ImageIcon(currentDirectory + "/unicornIcon.png").getImage().getScaledInstance(25, 25,
                        Image.SCALE_SMOOTH));

        // Create an array to hold the names
        String[] names = { "Select a Horse", "Horse", "Pony", "Donkey", "Unicorn" };

        // Create a ComboBoxModel
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        // Add each item to the model
        for (int i = 0; i < icons.length; i++) {
            model.addElement(new Object[] { icons[i], names[i] });
        }

        // Create the JComboBox and set its model
        breedsMenu = new JComboBox(model);

        // Set a custom renderer to display the icons
        breedsMenu.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                Object[] item = (Object[]) value;
                ImageIcon icon = (ImageIcon) item[0];
                String name = (String) item[1];
                label.setIcon(icon);
                label.setText(name);
                return label;
            }
        });

        breedsMenu.setMaximumSize(new Dimension(100, 30));
        breedsMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        horseDesignPanel.add(breedsMenu);

        JLabel accessoriesLabel = new JLabel(
                "What accessories would you like horse " + currentHorseCount + " to have?");
        accessoriesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accessoriesLabel.setForeground(Color.white);
        horseDesignPanel.add(accessoriesLabel);

        accessoriesMenu = new JComboBox();
        accessoriesMenu.addItem("Select an Accessory");
        accessoriesMenu.addItem("Saddle");
        accessoriesMenu.addItem("Horseshoes");
        accessoriesMenu.setMaximumSize(new Dimension(100, 30));
        horseDesignPanel.add(accessoriesMenu);

        nameTextFields.add(nameTextField);
        confidenceTextFields.add(confidenceTextField);
        currentHorseCount++;
    }

    private void startGUI() {

        // Gets rid of menu window as it is no longer needed
        frame.dispose();

        // Create a new RaceFrame
        raceFrame = new RaceFrame(trackColour, raceLength, horseNum, horses, user);

        JButton startButton = new JButton("Start Race");
        startButton.setEnabled(true);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null) {
                    timer.start(); // Start the timer
                    startButton.setEnabled(false); // Disable the start button
                    addHorsebtn.setEnabled(false); // Disable the "Add Horse" button
                    raceFrame.disableBetBtn();
                }
            }
        });

        JButton restartButton = new JButton("New Race");
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null) {
                    timer.stop(); // Stop the timer if it's running
                    timer = null; // Set the timer reference to null
                }
                raceFrame.dispose(); // Close the current
                if (statsFrame != null) {
                    statsFrame.dispose(); // Close the stats frame
                }
                startGUI(); // Restart the race when the button is clicked
            }
        });

        JButton statsButton = new JButton("Show Stats");
        statsButton.addActionListener(e -> showStats());

        addHorsebtn = new JButton("Add Horse");
        addHorsebtn.addActionListener(e -> {
            addHorseWindow();
        });
        if (horses.size() >= horseNum) {
            // Disable the "Add Horse" button
            addHorsebtn.setEnabled(false);
        }

        raceFrame.enableBetBtn();

        // reset all the lanes (all horses not fallen and back to 0).
        for (Horse horse : horses) {
            if (horse != null) {
                horse.goBackToStart();
            }
        }

        long startTime = System.currentTimeMillis();

        // Create a Timer that updates the horse positions and repaints the GUI every
        // 100 milliseconds
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the positions of the horses

                for (Horse horse : horses) {
                    if (horse != null) {
                        moveHorse(horse);
                    }
                }

                // update horse detail label
                for (int i = 0; i < horses.size(); i++) {
                    raceFrame.updateHorseDetails(i);
                }

                // Check if any horse has won the race
                int currentHorse = 0;
                for (Horse horse : horses) {
                    if (horse != null && raceWonBy(horse)) {
                        ((Timer) e.getSource()).stop(); // Stop the timer
                        // Display a message that the race is over
                        JOptionPane.showMessageDialog(raceFrame, "The race is over!");
                        // Display the winner
                        long endTime = System.currentTimeMillis();
                        double finishingTime = (endTime - startTime) / 1000.0; // convert to seconds

                        JOptionPane.showMessageDialog(raceFrame, "And the winner is " + horse.getName() + " ");
                        horse.updatePerformanceMetrics(finishingTime, 1);
                        for (Horse otherhorse : horses) {
                            if (otherhorse != null && otherhorse != horse) {
                                otherhorse.updatePerformanceMetricsLoss(finishingTime);
                            }
                        }

                        if (user.getHorseSelected() == currentHorse) {
                            user.updateBalance(user.getBetAmount());
                            user.setBetAmount(0);
                            user.setHorseSelected(-1);
                        } else {
                            user.updateBalance(-user.getBetAmount());
                            user.setBetAmount(0);
                            user.setHorseSelected(-1);
                        }

                        // Finalise performance metrics and print them
                        for (Horse allhorses : horses) {
                            if (allhorses != null) {
                                allhorses.finalisePerformanceMetrics();
                            }
                        }

                        restartButton.setEnabled(true);

                        if (horses.size() < horseNum) {
                            addHorsebtn.setEnabled(true);
                        } else {
                            addHorsebtn.setEnabled(false);
                        }

                        break;

                    } else if (allHorsesHaveFallen()) {

                        ((Timer) e.getSource()).stop(); // Stop the timer
                        JOptionPane.showMessageDialog(raceFrame, "All horses have fallen!");

                        user.updateBalance(-user.getBetAmount());

                        long endTime = System.currentTimeMillis();
                        double finishingTime = (endTime - startTime) / 1000.0; // convert to seconds

                        // Update performance metrics for all horses
                        for (Horse otherhorse : horses) {
                            if (otherhorse != null) {
                                otherhorse.updatePerformanceMetricsLoss(finishingTime);
                            }
                        }

                        // Finalise performance metrics and print them
                        for (Horse allhorses : horses) {
                            if (allhorses != null) {
                                allhorses.finalisePerformanceMetrics();
                            }
                        }

                        restartButton.setEnabled(true);

                        if (horses.size() < horseNum) {
                            addHorsebtn.setEnabled(true);
                        } else {
                            addHorsebtn.setEnabled(false);
                        }

                        break;
                    }
                    currentHorse++;
                }

                // Repaint the GUI to reflect the new positions of the horses
                raceFrame.repaint();

                // update bets label
                raceFrame.updateStatDetails(user);

            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        menuBar.add(menu);
        menuBar.add(startButton);
        menuBar.add(restartButton);
        menuBar.add(statsButton);
        menuBar.add(addHorsebtn);

        raceFrame.add(menuBar, BorderLayout.NORTH);

    }

    private void showStats() {
        statsFrame = new JFrame("Horse Stats");
        statsFrame.setSize(300, 300);
        statsFrame.setLayout(new BorderLayout());

        JPanel statspanel = new JPanel();
        statspanel.setLayout(new BoxLayout(statspanel, BoxLayout.Y_AXIS));
        statsFrame.add(statspanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Horse Stats");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        statspanel.add(title);

        // Create a JComboBox to hold the horse names
        JComboBox<String> horseComboBox = new JComboBox<>();
        horseComboBox.addItem("Select a horse");
        int horsenum = 1;
        for (Horse horse : horses) {
            if (horse != null) {
                horseComboBox.addItem("Horse " + horsenum + ": " + horse.getName());
                horsenum++;
            }
        }
        horseComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        statspanel.add(horseComboBox);

        // Create a panel to hold the performance metric labels
        JPanel metricsPanel = new JPanel();
        metricsPanel.setLayout(new BoxLayout(metricsPanel, BoxLayout.Y_AXIS));
        statspanel.add(metricsPanel);

        // Add an action listener to the JComboBox
        horseComboBox.addActionListener(e -> {
            // Clear the metrics panel
            metricsPanel.removeAll();

            // Get the selected horse
            int selectedHorseIndex = horseComboBox.getSelectedIndex() - 1;
            Horse selectedHorse = horses.get(selectedHorseIndex);

            // Display the performance metric labels for the selected horse
            if (selectedHorse != null) {
                JLabel[] labels = selectedHorse.returnPerformanceMetricsLabels();
                for (JLabel label : labels) {
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    metricsPanel.add(label);
                }
                metricsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            // Refresh the stats frame
            statsFrame.revalidate();
            statsFrame.repaint();
        });

        statsFrame.setVisible(true);
    }

    private void changeBackgroundColor(JComponent component, Color color) {
        component.setBackground(color);
        Component[] components = component.getComponents();
        for (Component comp : components) {
            if (comp instanceof JComponent) {
                changeBackgroundColor((JComponent) comp, color);
            }
        }
    }

    private void changeTextColor(JComponent component, Color color) {
        component.setForeground(color);
        Component[] components = component.getComponents();
        for (Component comp : components) {
            if (comp instanceof JComponent) {
                changeTextColor((JComponent) comp, color);
            }
        }
    }
}