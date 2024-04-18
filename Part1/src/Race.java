import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }


    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber == 1)
        {
            lane1Horse = theHorse;
        }
        else if (laneNumber == 2)
        {
            lane2Horse = theHorse;
        }
        else if (laneNumber == 3)
        {
            lane3Horse = theHorse;
        }
        else
        {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //create and add the horses
        createAndAddHorses();

        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0).
        if (lane1Horse != null) {
            lane1Horse.goBackToStart();
        }

        if (lane2Horse != null) {
            lane2Horse.goBackToStart();
        }

        if (lane3Horse != null) {
            lane3Horse.goBackToStart();
        }
                      
        while (!finished)
        {
            //move each horse
            if (lane1Horse != null) {
                moveHorse(lane1Horse);
            }

            if (lane2Horse != null) {
                moveHorse(lane2Horse);
            }

            if (lane3Horse != null) {
                moveHorse(lane3Horse);
            }
                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            if ( raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse) )
            {
                finished = true;

                if (raceWonBy(lane1Horse)) {
                    System.out.println("And the winner is "+lane1Horse.getName() + " ");
                } else if (raceWonBy(lane2Horse)) {
                    System.out.println("And the winner is "+lane2Horse.getName() + " ");
                } else {
                    System.out.println("And the winner is "+lane3Horse.getName() + " ");
                }
            }

            if ((lane1Horse == null || lane1Horse.hasFallen()) &&
                (lane2Horse == null || lane2Horse.hasFallen()) &&
                (lane3Horse == null || lane3Horse.hasFallen())) {
                    finished = true;
            }       
           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
    }

    public void changeHorseConfidence(Horse horse) {
        
        if (horse != null) {
            if (raceWonBy(horse)) {
                horse.setConfidence(horse.getConfidence() + 0.1);
            } else if (horse.hasFallen()) {
                horse.setConfidence(horse.getConfidence() - 0.1);
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
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
               changeHorseConfidence(theHorse);
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                changeHorseConfidence(theHorse);
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse != null && theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        if (lane1Horse != null)
        {
            printLane(lane1Horse);
            System.out.println();
        }
        if (lane2Horse != null)
        {
            printLane(lane2Horse);
            System.out.println();
        }
        if (lane3Horse != null)
        {
            printLane(lane3Horse);
            System.out.println();
        }

        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u2322');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');

        //print the horse's name and confidence
        System.out.print(" " + theHorse.getName() + " (Current confidence " + String.format("%.1f",theHorse.getConfidence()) + ")");
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
