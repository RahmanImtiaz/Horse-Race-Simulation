import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A horse with a name, a symbol, a distance travelled, a fallen status, and a confidence level.
 * 
 * @author Rahman Imtiaz
 * @version 1.0
 */

public class Horse
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean fallen;
    private double confidence;
    private String breed;
    private ImageIcon breedIcon;
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

    // for gui
    public Horse()
    {
       this.name = "UNDEFINED";
       this.breed = "UNDEFINED";
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

        if (newConfidence > 1.0) {
            newConfidence = 1.0;
        } else if (newConfidence < 0.0) {
            newConfidence = 0.0;
        }

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

    public void setBreed(String newBreed)
    {
        this.breed = newBreed;
    }

    public String getBreed()
    {
        return this.breed;
    }

    public void setBreedIcon(ImageIcon newIcon) {
        int width = newIcon.getIconWidth();
        int height = newIcon.getIconHeight();
    
        // Create a new image that can support transparency
        BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        // Get the graphics object from the image
        Graphics2D g = combinedImage.createGraphics();
    
        // Set the color and draw the circle
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, width, height);
    
        // Draw the icon on top of the circle
        g.drawImage(newIcon.getImage(), 0, 0, null);
    
        // Dispose the graphics object
        g.dispose();
    
        // Create a new ImageIcon from the combined image
        this.breedIcon = new ImageIcon(combinedImage);
    }

    public ImageIcon getBreedIcon()
    {
        return this.breedIcon;
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
        finishingTime = Math.round(finishingTime * 100.0) / 100.0;
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
        if (this.speeds.size() == 0){
            this.avgSpeed = 0;
        } else {
            this.avgSpeed = sum/this.speeds.size();
        }
       
    }

    public JLabel[] returnPerformanceMetricsLabels()
    {
        JLabel[] labels = new JLabel[6];
        labels[0] = new JLabel("Horse: " + this.name);
        labels[1] = new JLabel("Avg Speed: " + String.format("%.2f", this.avgSpeed));
        labels[2] = new JLabel("Total races: " + this.totalRaces);
        labels[3] = new JLabel("Total wins: " + this.totalWins);
        if (this.totalRaces == 0) {
            labels[4] = new JLabel("Win ratio: 0%");
        } else {
            labels[4] = new JLabel("Win ratio: " + String.format("%.2f", ((double)this.totalWins/this.totalRaces)*100) + "%");
        }
        labels[5] = new JLabel("Finishing times: " + this.finishingTimes);

        return labels;
    }
    
    public double calculateOdds(double trackLength) {
        double speedFactor = this.avgSpeed != 0 ? 1 / this.avgSpeed : 0;
    
        double winRatioFactor = this.totalRaces != 0 ? 1 - ((double) this.totalWins / this.totalRaces) : 1;
    
        double recentSpeedFactor = 0;
        int recentRaces = 5;
        int count = 0;
        for (int i = this.speeds.size() - 1; i >= 0 && count < recentRaces; i--, count++) {
            if (!Double.isNaN(this.speeds.get(i))) {
                recentSpeedFactor += this.speeds.get(i);
            }
        }
        recentSpeedFactor = count > 0 ? 1 / (recentSpeedFactor / count) : 0;
    
        double confidenceFactor = 1 - this.confidence;
    
        double trackLengthFactor = trackLength / 1000;
    
        double odds = (speedFactor + winRatioFactor + recentSpeedFactor + confidenceFactor + trackLengthFactor) / 5;
    
        return odds;
    }
}
