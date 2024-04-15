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




    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
       this.name = horseName;
       this.symbol = horseSymbol;
       this.confidence = horseConfidence;
    }

    public Horse()
    {
       this.name = "UNDEFINED";
       this.horseLabel = null;
       this.confidence = 0.0;
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
    }
    
    public boolean hasFallen()
    {
        return this.fallen;
    }

    public void moveForward() {
        this.distanceTravelled += 10; // Move the horse 10 units forward
        this.horseLabel.setLocation(this.distanceTravelled, this.horseLabel.getLocation().y); // Update the JLabel's position
    }

    public void setConfidence(double newConfidence)
    {
        this.confidence = newConfidence;
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
}
