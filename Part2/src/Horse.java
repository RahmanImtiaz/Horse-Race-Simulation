import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean fallen;
    private double confidence;
    private JLabel horseLabel;
    private List<Double> speeds;
    private double avgSpeed;
    private int totalRaces;
    private int totalWins;
    private List<Double> finishingTimes;




    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
       this.name = horseName;
       this.symbol = horseSymbol;
       this.confidence = horseConfidence;
       this.avgSpeed = 0;
       this.totalRaces = 0;
       this.totalWins = 0;
       this.speeds = new ArrayList<>();
       this.finishingTimes = new ArrayList<>();
    }

    public Horse()
    {
       this.name = "UNDEFINED";
       this.horseLabel = null;
       this.confidence = 0.0;
       this.avgSpeed = 0;
       this.totalRaces = 0;
       this.totalWins = 0;
       this.speeds = new ArrayList<>();
       this.finishingTimes = new ArrayList<>();
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        this.fallen = true;
    }
    
    public double getConfidence()
    {
        return this.confidence;
    }
    
    public int getDistanceTravelled()
    {
        return this.distanceTravelled;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public char getSymbol()
    {
        return this.symbol;
    }
    
    public void goBackToStart()
    {
        this.distanceTravelled = 0;
        this.fallen = false;
        this.horseLabel.setLocation(50, this.horseLabel.getLocation().y);
        
    }
    
    public boolean hasFallen()
    {
        return this.fallen;
    }

    public void moveForward() {
        this.distanceTravelled += 10; // Move the horse 10 units forward
        this.horseLabel.setLocation(this.horseLabel.getLocation().x + 10, this.horseLabel.getLocation().y); // Update the JLabel's position
    }

    public void setConfidence(double newConfidence)
    {
        DecimalFormat df = new DecimalFormat("#.0");
        this.confidence = Double.valueOf(df.format(newConfidence));
    }
    
    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }
    
    public void setHorseGUI(JLabel newLabel)
    {
        this.horseLabel = newLabel;
    }

    public JLabel getHorseGUI()
    {
        return this.horseLabel;
    }

    public  JLabel horseFallenGUI()
    {
        if (this.fallen == true)
        {
            this.horseLabel.setText("X");
        }
        return this.horseLabel;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public void updatePerformanceMetrics(double finishingTime, int winsIncrease)
    {
        this.speeds.add(this.distanceTravelled/finishingTime);
        this.totalRaces++;
        this.totalWins = this.totalWins + winsIncrease;
        this.finishingTimes.add(finishingTime);
    }

    public void updatePerformanceMetricsLoss(double timeTaken) {
        this.speeds.add(this.distanceTravelled/timeTaken);
        this.totalRaces++;
    }

    public void finalisePerformanceMetrics()
    {
        double sum = 0;
        for (double speed : this.speeds)
        {
            sum += speed;
        }
        this.avgSpeed = sum/this.speeds.size();
    }

    public void printPerformanceMetrics()
    {
        System.out.println("Horse " + this.name);
        System.out.println("Avg Speed" + this.avgSpeed);
        System.out.println("Total races" + this.totalRaces);
        System.out.println("Total wins" + this.totalWins);
        System.out.println("Win ratio:" + ((double)this.totalWins/this.totalRaces)*100 + "%");
        System.out.println("Finishing times" + this.finishingTimes);
    }

    public JLabel[] returnPerformanceMetricsLabels()
    {
        JLabel[] labels = new JLabel[6];
        labels[0] = new JLabel("Horse: " + this.name);
        labels[1] = new JLabel("Avg Speed: " + this.avgSpeed);
        labels[2] = new JLabel("Total races: " + this.totalRaces);
        labels[3] = new JLabel("Total wins: " + this.totalWins);
        labels[4] = new JLabel("Win ratio: " + ((double)this.totalWins/this.totalRaces)*100 + "%");
        labels[5] = new JLabel("Finishing times: " + this.finishingTimes);

        return labels;
    }
    
}
