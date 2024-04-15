import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race {
    private int raceLength;
    private Color trackColour;
    private int horseNum;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;
    private JPanel panel;
    private JFrame frame;
    private Timer timer;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) {
        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse   the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber == 1) {
            lane1Horse = theHorse;
        } else if (laneNumber == 2) {
            lane2Horse = theHorse;
        } else if (laneNumber == 3) {
            lane3Horse = theHorse;
        } else {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {
        // create and add the horses
        createAndAddHorses();

        // declare a local variable to tell us when the race is finished
        boolean finished = false;

        // reset all the lanes (all horses not fallen and back to 0).
        if (lane1Horse != null) {
            lane1Horse.goBackToStart();
        }

        if (lane2Horse != null) {
            lane2Horse.goBackToStart();
        }

        if (lane3Horse != null) {
            lane3Horse.goBackToStart();
        }

        while (!finished) {
            // move each horse
            if (lane1Horse != null) {
                moveHorse(lane1Horse);
            }

            if (lane2Horse != null) {
                moveHorse(lane2Horse);
            }

            if (lane3Horse != null) {
                moveHorse(lane3Horse);
            }

            // print the race positions
            printRace();

            // if any of the three horses has won the race is finished
            if (raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse)) {
                finished = true;

                if (raceWonBy(lane1Horse)) {
                    System.out.println("And the winner is " + lane1Horse.getName() + " ");
                } else if (raceWonBy(lane2Horse)) {
                    System.out.println("And the winner is " + lane2Horse.getName() + " ");
                } else {
                    System.out.println("And the winner is " + lane3Horse.getName() + " ");
                }
            }

            if ((lane1Horse == null || lane1Horse.hasFallen()) &&
                    (lane2Horse == null || lane2Horse.hasFallen()) &&
                    (lane3Horse == null || lane3Horse.hasFallen())) {
                finished = true;
            }

            // wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
        }
    }

    public void createAndAddHorses() {
        Scanner sc = new Scanner(System.in);

        int laneNum = 1;
        String option = "";
        do {
            System.out.println("Enter the name of the horse: ");
            String name = sc.nextLine();
            System.out.println("Enter the symbol of the horse: ");
            char symbol = sc.nextLine().charAt(0);
            double confidence = enterConfidence("Enter the confidence of the horse (0-1): ");

            Horse horse = new Horse(symbol, name, confidence);
            addHorse(horse, laneNum);
            laneNum++;

            if (laneNum < 4) {
                option = enterOption("y", "n", "Would you like to add more horses? (y/n)");
            } else {
                option = "n";
            }

        } while (!option.equals("n"));
    }

    private String enterOption(String opt1, String opt2, String msg) {
        Scanner sc = new Scanner(System.in);
        String option = "";
        do {
            System.out.println(msg);
            option = sc.next();
        } while (!option.equals(opt1) && !option.equals(opt2));
        return option;
    }

    private double enterConfidence(String msg) {
        Scanner sc = new Scanner(System.in);
        double confidence = 0;
        boolean validInput = false;
        do {
            try {
                System.out.println(msg);
                confidence = sc.nextDouble();
                if (confidence >= 0 && confidence <= 1) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between 0 and 1.");
                }
            } catch (InputMismatchException e) {
                System.out.println("That's not a valid number!");
                sc.next(); // consume the invalid token
            }
        } while (!validInput);
        return confidence;
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
            }

            // the probability that the horse will fall is very small (max is 0.1)
            // but will also will depends exponentially on confidence
            // so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
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
        if (theHorse != null && theHorse.getDistanceTravelled() == (raceLength * 10)) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * Print the race on the terminal
     */
    private void printRace() {
        System.out.print('\u000C'); // clear the terminal window

        multiplePrint('=', raceLength + 3); // top edge of track
        System.out.println();

        if (lane1Horse != null) {
            printLane(lane1Horse);
            System.out.println();
        }
        if (lane2Horse != null) {
            printLane(lane2Horse);
            System.out.println();
        }
        if (lane3Horse != null) {
            printLane(lane3Horse);
            System.out.println();
        }

        multiplePrint('=', raceLength + 3); // bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race
     * for example
     * | X |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse) {
        // calculate how many spaces are needed before
        // and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        // print a | for the beginning of the lane
        System.out.print('|');

        // print the spaces before the horse
        multiplePrint(' ', spacesBefore);

        // if the horse has fallen then print dead
        // else print the horse's symbol
        if (theHorse.hasFallen()) {
            System.out.print('\u2322');
        } else {
            System.out.print(theHorse.getSymbol());
        }

        // print the spaces after the horse
        multiplePrint(' ', spacesAfter);

        // print the | for the end of the track
        System.out.print('|');

        // print the horse's name and confidence
        System.out.print(" " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ")");
    }

    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times) {
        int i = 0;
        while (i < times) {
            System.out.print(aChar);
            i = i + 1;
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
        JButton button1 = new JButton("Design Track");
        JButton button2 = new JButton("Customise Horses");
        JButton button3 = new JButton("Start Race");
        JButton button4 = new JButton("Exit");

        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
        button4.setAlignmentX(Component.CENTER_ALIGNMENT);
        button1.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add action listeners to buttons
        button1.addActionListener(e -> designTrack());
        button2.addActionListener(e -> customiseHorses());
        button3.addActionListener(e -> startGUI());
        button4.addActionListener(e -> System.exit(0));

        // Add components to panel
        panel.add(title);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);

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

        // Add components to the new panel
        JLabel title = new JLabel("Design Track");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(title);

        JLabel label1 = new JLabel("Enter the length of the track (in metres):");
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(label1);

        JTextField raceLengthTextField = new JTextField();
        raceLengthTextField.setMaximumSize(new Dimension(100, 30));
        raceLengthTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(raceLengthTextField);

        JLabel label3 = new JLabel("How many tracks (ie. Number of horses) would you like? (1-3)");
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(label3);

        JTextField numTracksTextField = new JTextField();
        numTracksTextField.setMaximumSize(new Dimension(100, 30));
        numTracksTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(numTracksTextField);

        JLabel label2 = new JLabel("What colour would you like the track to be?");
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(label2);

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackDesignPanel.add(colorChooser);

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> {
            try {
                int tempRaceLength = Integer.parseInt(raceLengthTextField.getText());
                int tempHorseNum = Integer.parseInt(numTracksTextField.getText());

                if (tempRaceLength > 0 && tempHorseNum > 0 && tempHorseNum <= 3) {
                    raceLength = tempRaceLength;
                    horseNum = tempHorseNum;
                    trackColour = colorChooser.getColor();
                    System.out.println("raceLength: " + raceLength + ", horseNum: " + horseNum + ", trackColour: "
                            + trackColour.toString());
                    customiseHorses();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input. Please enter a positive race length and a horse number between 1 and 3.");
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
        JPanel horseDesignPanel = new JPanel();
        horseDesignPanel.setLayout(new BoxLayout(horseDesignPanel, BoxLayout.Y_AXIS));

        // Create arrays to store the text fields
        JTextField[] nameTextFields = new JTextField[horseNum];
        JTextField[] confidenceTextFields = new JTextField[horseNum];

        for (int i = 1; i <= horseNum; i++) {

            if (i == 1) {
                lane1Horse = new Horse();
            } else if (i == 2) {
                lane2Horse = new Horse();
            } else {
                lane3Horse = new Horse();
            }

            JLabel Colourlabel = new JLabel("What colour would you like horse " + i + " to be?");
            Colourlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            horseDesignPanel.add(Colourlabel);

            JButton button = new JButton();
            button.setPreferredSize(new Dimension(300, 20));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            final int finalI = i; // Create a final copy of i
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color initialBackground = button.getBackground();
                    Color color = JColorChooser.showDialog(null, "JColorChooser Sample", initialBackground);
                    if (color != null) {
                        button.setBackground(color);

                        JLabel horseLabel = new JLabel(new HorseIcon(trackColour, color));
                        if (finalI == 1) { // Use finalI instead of i
                            lane1Horse.setHorseGUI(horseLabel);
                        } else if (finalI == 2) { // Use finalI instead of i
                            lane2Horse.setHorseGUI(horseLabel);
                        } else {
                            lane3Horse.setHorseGUI(horseLabel);
                        }
                    }
                }
            });
            horseDesignPanel.add(button);

            JLabel label = new JLabel("Enter the name of horse " + i + ":");
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            horseDesignPanel.add(label);

            JTextField nameTextField = new JTextField();
            nameTextField.setMaximumSize(new Dimension(100, 30));
            nameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
            horseDesignPanel.add(nameTextField);

            JLabel label2 = new JLabel("Enter the confidence of horse " + i + " (0-1):");
            label2.setAlignmentX(Component.CENTER_ALIGNMENT);
            horseDesignPanel.add(label2);

            JTextField confidenceTextField = new JTextField();
            confidenceTextField.setMaximumSize(new Dimension(100, 30));
            confidenceTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
            horseDesignPanel.add(confidenceTextField);

            nameTextFields[i - 1] = nameTextField;
            confidenceTextFields[i - 1] = confidenceTextField;

        }

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> {
            for (int i = 1; i <= horseNum; i++) {
                try {
                    String name = nameTextFields[i - 1].getText();
                    double confidence = Double.parseDouble(confidenceTextFields[i - 1].getText());

                    if (confidence >= 0 && confidence <= 1) {
                        if (i == 1) {
                            lane1Horse.setName(name);
                            lane1Horse.setConfidence(confidence);
                            System.out.println(
                                    "Name: " + lane1Horse.getName() + ", Confidence: " + lane1Horse.getConfidence());
                        } else if (i == 2) {
                            lane2Horse.setName(name);
                            lane2Horse.setConfidence(confidence);
                            System.out.println(
                                    "Name: " + lane2Horse.getName() + ", Confidence: " + lane2Horse.getConfidence());
                        } else {
                            lane3Horse.setName(name);
                            lane3Horse.setConfidence(confidence);
                            System.out.println(
                                    "Name: " + lane3Horse.getName() + ", Confidence: " + lane3Horse.getConfidence());
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 0 and 1.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                    return;
                }
            }

            startGUI();
        });

        horseDesignPanel.add(submit);

        // Add the new panel to the original panel
        panel.add(horseDesignPanel);

        // Refresh the panel
        panel.revalidate();
        panel.repaint();

    }

    private void startGUI() {
        // Gets rid of menu window as it is no longer needed
        frame.dispose();

        // Create a new RaceFrame
        RaceFrame raceFrame = new RaceFrame(trackColour, raceLength, horseNum, lane1Horse, lane2Horse, lane3Horse);

        // reset all the lanes (all horses not fallen and back to 0).
        if (lane1Horse != null) {
            lane1Horse.goBackToStart();
        }

        if (lane2Horse != null) {
            lane2Horse.goBackToStart();
        }

        if (lane3Horse != null) {
            lane3Horse.goBackToStart();
        }

        // update horse detail label
        for (int i = 0; i < horseNum; i++) {
            raceFrame.updateHorseDetails(i);
        }

        System.out.println("Horse 1 confidence: " + lane1Horse.getConfidence());
        System.out.println("Horse 2 confidence: " + lane2Horse.getConfidence());
        System.out.println("Horse 3 confidence: " + lane3Horse.getConfidence());

        // Create a Timer that updates the horse positions and repaints the GUI every
        // 100 milliseconds
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the positions of the horses
                Horse theHorse = null;
                if (lane1Horse != null) {
                    moveHorse(lane1Horse);
                }

                if (lane2Horse != null) {
                    moveHorse(lane2Horse);
                }

                if (lane3Horse != null) {
                    moveHorse(lane3Horse);
                }

                // Check if any horse has won the race
                if (raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse)) {
                    ((Timer) e.getSource()).stop(); // Stop the timer
                    // Display a message that the race is over
                    JOptionPane.showMessageDialog(raceFrame, "The race is over!");
                    // Display the winner
                    if (raceWonBy(lane1Horse)) {
                        JOptionPane.showMessageDialog(raceFrame, "And the winner is " + lane1Horse.getName() + " ");
                    } else if (raceWonBy(lane2Horse)) {
                        JOptionPane.showMessageDialog(raceFrame, "And the winner is " + lane2Horse.getName() + " ");
                    } else {
                        JOptionPane.showMessageDialog(raceFrame, "And the winner is " + lane3Horse.getName() + " ");
                    }
                }

                // Repaint the GUI to reflect the new positions of the horses
                raceFrame.repaint();
            }
        });
        timer.start(); // Start the timer


        JButton restartButton = new JButton("New Race");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null) {
                    timer.stop(); // Stop the timer if it's running
                    timer = null; // Set the timer reference to null
                }
                raceFrame.dispose(); // Close the current
                startGUI(); // Restart the race when the button is clicked
            }
        });
        raceFrame.add(restartButton, BorderLayout.SOUTH); // Add the button to the bottom of the frame

    }

}
