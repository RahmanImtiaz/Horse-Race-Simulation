# Horse-Race-Simulation

This is a Horse Race Simulation project that uses Java GUI to simulate a horse race. The simulation includes a betting system and customization of horses and tracks. This project was created as a part of Year 1 OOP Project.

The project is divided into two parts: `Part1` and `Part2`. `Part1` contains the initial implementation of the horse race simulation without a GUI. `Part2` extends `Part1` by adding a GUI and additional features like a betting system and customization of horses and tracks.

## Getting Started

### Setup Steps

1. Ensure you have Java installed on your system. If not, you can download it from [here](https://www.oracle.com/uk/java/technologies/downloads/#java21).
2. Clone this repository to your local machine using `git clone <repository-url>`.
3. Navigate to the part 1 or part 2 directory depending on which part of the project you want to run. Part 1 to run the textual implementation, and Part 2 to run the GUI implementation.

### Dependencies

This project is built with Java and uses the Swing library for the graphical user interface in `Part2`. No other dependencies are required. Make sure you have a Java Development Kit (JDK) installed on your machine. You can download the latest JDK from Oracle's [Official Website](https://www.oracle.com/uk/java/technologies/downloads/#java21).

### Usage Guidelines (Command Line)

1. To run the program, navigate to the directory containing `main.java` in either `Part1` or `Part2`.
2. Compile the Java files using the command `javac *.java`.
3. Run the program using the command `java main`.

### Using an IDE Instead

If you prefer to use an Integrated Development Environment (IDE) instead of the command line, you can do so. Here are the general steps:

1. Open your preferred IDE (Visual Studio Code, IntelliJ IDEA, Eclipse, etc.).
2. Import/Open the project into your IDE. This usually involves selecting 'Open' or 'Import' from the 'File' menu and then navigating to the project directory.
3. Once the project is imported/Opened, you should be able to navigate through the project files using the IDE's project explorer.
4. To run the program, locate the `main.java` file in either `Part1` or `Part2`, right-click on it and select 'Run' or 'Debug'. The exact steps may vary depending on your IDE.

Please refer to your IDE's documentation for more specific instructions.

## Files in the Project

- `Horse.java`: This file contains the Horse class which is used to create horse objects for the simulation.
- `HorseIcon.java`: This file is used to handle the horse icons in the GUI (only in `Part2`).
- `Race.java`: This file contains the Race class which is used to simulate the horse race.
- `RaceFrame.java`: This file is used to create the GUI frame for the race (only in `Part2`).
- `User.java`: This file contains the User class which is used to handle user interactions in the betting system (only in `Part2`).
- `main.java`: This is the main class that runs the program.

## Contributing

Contributions are welcome. Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.