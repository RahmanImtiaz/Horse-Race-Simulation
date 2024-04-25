import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Rahman Imtiaz
 * @version 1.0
 */
public class Race {
    private int raceLength;
    private List<Horse> horses;
    private int laneAmount;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) {
        // initialise instance variables
        raceLength = distance;
        laneAmount = 2; // default 2 lanes (minimum 2 horses)
        horses = new ArrayList<Horse>();
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse   the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    private void addHorse(Horse theHorse) {
        horses.add(theHorse);
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */

    public void startRace() {

        // create and add the horses
        raceLength = enterRaceLength();
        createAndAddHorses();
        laneAmount = enterLaneAmount(horses.size(), 12, "Enter the amount of lanes (" + horses.size() + "-12)");
        String option = "";
        int loopCount = 1;
        do {

            if (loopCount > 1) {
                addMoreHorses();
            }
            // start game loop
            startRaceGameLoop();
            loopCount++;
            option = enterOption("y", "n", "Would you like to start a new Race? (y/n)");

        } while (!option.equals("n"));
    }

    public int enterRaceLength() {

        String choice = "";
        int newRaceLength = raceLength;
        do {
                choice = enterOption("y", "n", "Your current race length is " + raceLength + ". Would you like to change it? (y/n)");
                if (choice.equals("y")) {
                    newRaceLength = enterLaneAmount(10, 150, "Enter the new race length (10-150)");
                    return newRaceLength;
                }
        } while (!choice.equals("n"));

        return newRaceLength;
    }

    public void addMoreHorses() {

        String addHorseChoice = "";
        int horseCount = horses.size();
        do {

            if (horseCount < laneAmount) {
                addHorseChoice = enterOption("y", "n", "Would you like to add a new horse? (y/n)");
                if (addHorseChoice.equals("y")) {
                    createAndAddOneHorse();
                }
                horseCount = horses.size();
            } else {
                addHorseChoice = "n"; // maximum 12 horses
            }
        } while (!addHorseChoice.equals("n"));
    }

    public void createAndAddOneHorse() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of the horse: ");
        String name = sc.nextLine();
        char symbol = enterHorseSymbol();
        double confidence = enterConfidence("Enter the confidence of the horse (0-1): ");

        Horse horse = new Horse(symbol, name, confidence);
        addHorse(horse);
    }

    public void startRaceGameLoop() {

        // declare a local variable to tell us when the race is finished
        boolean finished = false;

        // reset all the lanes (all horses not fallen and back to 0).
        for (Horse horse : horses) {
            horse.goBackToStart();
        }

        while (!finished) {
            // move each horse
            for (Horse horse : horses) {
                if (horse != null) {
                    moveHorse(horse);
                }
            }

            // print the race positions
            printRace();

            // if any of the three horses has won the race is finished
            for (Horse horse : horses) {
                if (horse != null && raceWonBy(horse)) {
                    finished = true;
                    System.out.println("And the winner is " + horse.getName() + " ");
                    break;
                }
            }

            if (allHorsesHaveFallen()) {
                finished = true;
            }

            // wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
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

    private void createAndAddHorses() {
        Scanner sc = new Scanner(System.in);

        int laneNum = 1;
        String option = "";
        do {
            System.out.println("Enter the name of the horse: ");
            String name = sc.nextLine();
            char symbol = enterHorseSymbol();
            double confidence = enterConfidence("Enter the confidence of the horse (0-1): ");

            Horse horse = new Horse(symbol, name, confidence);
            addHorse(horse);
            laneNum++;

            if (laneNum < 3) {
                System.out.println("Creating a second horse...");
                option = "y"; // minimum 2 horses
            } else if (laneNum < 13) {
                option = enterOption("y", "n", "Would you like to add more horses? (y/n)");
            } else {
                option = "n"; // maximum 12 horses
            }

        } while (!option.equals("n"));
    }

    private String enterOption(String opt1, String opt2, String msg) {
        Scanner sc = new Scanner(System.in);
        String option = "";
        do {
            System.out.println(msg);
            option = sc.nextLine();
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

    private char enterHorseSymbol() {
        Scanner sc = new Scanner(System.in);
        char symbol = ' ';
        boolean validInput = false;
        do {
            try {
                System.out.println("Enter the symbol of the horse: ");
                String input = sc.nextLine();
                if (input.length() > 0) {
                    symbol = input.charAt(0);
                    if (!Character.isWhitespace(symbol)) {
                        validInput = true;
                    } else {
                        System.out.println("Please enter a valid character.");
                    }
                } else {
                    System.out.println("Please enter something.");
                }
            } catch (InputMismatchException e) {
                System.out.println("That's not a valid character!");
                sc.next(); // consume the invalid token
            }
        } while (!validInput);
        return symbol;
    }

    private int enterLaneAmount(int min, int max, String msg) {
        Scanner sc = new Scanner(System.in);
        int amount = 2; // default 2 lanes
        boolean validInput = false;
        do {
            try {
                System.out.println(msg);
                amount = sc.nextInt();
                if (amount >= min && amount <= max) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("That's not a valid number!");
                sc.next(); // consume the invalid token
            }
        } while (!validInput);
        return amount;
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse) {
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
                changeHorseConfidence(theHorse);
            }

            // if the horse has not moved forward at all, then it has fallen
            if (theHorse.getConfidence() == 0) {
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
        if (theHorse != null && theHorse.getDistanceTravelled() == raceLength) {
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

        for (Horse horse : horses) {
            if (horse != null) {
                printLane(horse);
                System.out.println();
            }
        }
        for (int i = 0; i < laneAmount - horses.size(); i++) {
            printEmptyLane();
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
        System.out.print(" " + theHorse.getName() + " (Current confidence "
                + String.format("%.1f", theHorse.getConfidence()) + ")");
    }

    private void printEmptyLane() {
        // print a | for the beginning of the lane
        System.out.print('|');

        // print the spaces for the entire lane
        multiplePrint(' ', raceLength + 1);

        // print the | for the end of the track
        System.out.print('|');

        // print a newline to move to the next line
        System.out.println();
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
}
