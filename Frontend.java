
import java.util.Scanner;
import java.util.List;
import java.io.IOException;

/**
 * The Frontend class implements the FrontendInterface and provides a command-line interface
 * for interacting with i-songly. It allows the user to load songs, filter songs, retrieve songs by energy level,
 * and display the top five most recent songs. The class communicates with a backend to perform operations on
 * song data.
 */
public class Frontend implements FrontendInterface {
  private Scanner scanner;
  private BackendInterface backend;

  /**
   * Constructs a new Frontend object that provides interaction with the backend system.
   * Throws an IllegalArgumentException if the backend is null.
   *
   * @param in      The Scanner object for user input.
   * @param backend The BackendInterface implementation for data operations.
   */
  public Frontend(Scanner in, BackendInterface backend) {
    if (backend == null) {
      throw new IllegalArgumentException("Backend cannot be null");
    }
    this.scanner = in;
    this.backend = backend;
  }

  /**
   * Runs the main command loop of the application, allowing users to interact with various features
   * such as loading songs, retrieving songs by energy, and applying filters.
   * The loop continues until the user chooses to quit.
   */
  @Override
  public void runCommandLoop() {
    boolean running = true;

    while (running) {
      displayMainMenu(); // Display menu options
      String command = scanner.nextLine().trim().toUpperCase(); // Get user input

      switch (command) {
        case "L":
          loadFile(); // Load song data
          break;
        case "G":
          getSongs(); // Retrieve songs by energy
          break;
        case "F":
          setFilter(); // Set a filter on songs
          break;
        case "D":
          displayTopFive(); // Display top 5 most recent songs
          break;
        case "Q":
          running = false; // Exit the loop
          break;
        default:
          System.out.println("Invalid command. Please try again."); // Handle invalid commands
      }
    }
  }

  /**
   * Displays the main menu options to the user, including commands to load files,
   * retrieve songs by energy, filter by danceability, and display recent songs.
   */
  @Override
  public void displayMainMenu() {
    System.out.println("Main Menu:");
    System.out.println("[L] Load Song File");
    System.out.println("[G] Get Songs by Energy");
    System.out.println("[F] Filter Songs by Danceability");
    System.out.println("[D] Display Five Most Recent Songs");
    System.out.println("[Q] Quit");
  }

  /**
   * Prompts the user to enter a filename and attempts to load the song data from the file.
   * If an invalid filename is provided, the user is prompted again until a valid file is loaded
   * or the user chooses to return to the main menu.
   */
  @Override
  public void loadFile() {
    while (true) {
      System.out.println("Please enter the filename to load (or 'Q' to return to main menu):");
      String filename = scanner.nextLine();

      if (filename.equalsIgnoreCase("Q")) return; // Exit if 'Q' is entered

      try {
        backend.readData(filename); // Load data from backend
        System.out.println("File loaded successfully.");
        return;
      } catch (IOException e) {
        System.out.println("Error loading file. Please try again with a valid file."); // Handle file load errors
      }
    }
  }

  /**
   * Prompts the user for minimum and maximum energy values and retrieves songs within that range from the backend.
   * Both values are optional, but at least one must be provided. If invalid input is entered, the user is prompted again.
   */
  @Override
  public void getSongs() {
    Integer minEnergy = null;
    Integer maxEnergy = null;

    // Prompt for minimum energy
    while (minEnergy == null) {
      System.out.println("Enter minimum Energy (or press Enter to skip):");
      String minStr = scanner.nextLine().trim();

      if (minStr.isEmpty()) {
        System.out.println("Skipping minimum energy.");
        break;
      } else {
        minEnergy = parseInteger(minStr, "Minimum Energy");
        if (minEnergy == null) {
          System.out.println("Invalid input for minimum energy. Please enter a valid number.");
        }
      }
    }

    // Prompt for maximum energy
    while (maxEnergy == null) {
      System.out.println("Enter maximum Energy (or press Enter to skip):");
      String maxStr = scanner.nextLine().trim();

      if (maxStr.isEmpty()) {
        System.out.println("Skipping maximum energy.");
        break;
      } else {
        maxEnergy = parseInteger(maxStr, "Maximum Energy");
        if (maxEnergy == null) {
          System.out.println("Invalid input for maximum energy. Please enter a valid number.");
        }
      }
    }

    // Check if both inputs are null
    if (minEnergy == null && maxEnergy == null) {
      System.out.println("Please enter at least one valid numerical range (minimum or maximum energy).");
      return;
    }

    // Retrieve and display songs within the specified energy range
    List<String> songs = backend.getRange(minEnergy, maxEnergy);

    if (songs.isEmpty()) {
      System.out.println("No songs found in the specified energy range.");
    } else {
      System.out.println("Songs in the specified energy range:");
      for (String song : songs) {
        System.out.println(song);
      }
    }
  }

  /**
   * Allows the user to set or clear a danceability filter for the songs. If a valid threshold is provided,
   * the filter is applied and the matching songs are displayed. If 'clear' is entered, the filter is removed.
   */
  @Override
  public void setFilter() {
    System.out.println("Enter Danceability filter (or type 'clear' to remove filter):");
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("clear")) {
      backend.setFilter(null); // Clear the filter
      System.out.println("Danceability filter cleared.");
    } else {
      try {
        int threshold = Integer.parseInt(input); // Set filter with user input
        List<String> filteredSongs = backend.setFilter(threshold);
        System.out.println("Danceability filter set to: " + threshold);

        if (filteredSongs.isEmpty()) {
          System.out.println("No songs match the current danceability filter.");
        } else {
          System.out.println("Number of songs matching the filter: " + filteredSongs.size());
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number or type 'clear' to remove the filter.");
      }
    }
  }

  /**
   * Displays the titles of up to five of the most recent songs within the previously set energy range.
   * If no songs are available, the user is prompted to adjust the energy range or filter.
   */
  @Override
  public void displayTopFive() {
    List<String> topFiveSongs = backend.fiveMost();

    if (topFiveSongs.isEmpty()) {
      System.out.println("No songs available. Please adjust the energy range or remove the danceability filter.");
    } else {
      System.out.println("Top 5 most recent songs:");
      for (String song : topFiveSongs) {
        System.out.println(song);
      }
    }
  }

  /**
   * Utility method to parse an integer from a string with error handling.
   *
   * @param s the string input to parse
   * @param field the field name for the error message
   * @return the parsed integer or null if invalid
   */
  private Integer parseInteger(String s, String field) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Invalid " + field + ". Please enter a valid number.");
      return null;
    }
  }
}
