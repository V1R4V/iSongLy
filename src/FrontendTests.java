import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.io.IOException;

/**
 * The FrontendTester class contains unit tests for the Frontend class.
 * It tests various functionalities of the Frontend class using JUnit 5.
 */
public class FrontendTests {

  private Frontend frontend;
  private BackendInterface backend;



  /**
   * Sets up the test environment before each test method. This method initializes the backend and
   * frontend objects used in the tests.
   */
  @BeforeEach
  public void setUp() {
    IterableSortedCollection<Song> tree = new Tree_Placeholder(); //  tree placeholder
    backend = new Backend_Placeholder(tree);                      // Backend placeholder
    Scanner scanner = new Scanner(System.in);                     // Empty scanner for testing
    frontend = new Frontend(scanner, backend);                    // Create the frontend instance

  }


  /**
   * Tests the loadFile method of the Frontend class. This test simulates loading a file and checks
   * if songs are loaded correctly.
   *
   * @throws IOException if there's an error reading the file
   */
  @Test
  public void roleTest1() throws IOException {
    // Test behavior when loading a file
    Scanner scanner = new Scanner("testFile.txt\nQ");  // Simulate input
    frontend = new Frontend(scanner, backend);

    frontend.loadFile();

    List<String> songs = backend.fiveMost();

    // Assuming that a file was loaded successfully, there should be song(s)
    //especially this one because this loads only when file is loaded hence
    //the backend placeholder is called and that adds this song to the list
    assertTrue(songs.contains("DJ Got Us Fallin' In Love (feat. Pitbull)"));
  }

  /**
   * Tests the getSongs method of the Frontend class. This test simulates user input to get songs
   * within a specific energy range.
   */
  @Test
  public void roleTest2() {
    // Simulate user input to get songs with energy range
    Scanner scanner = new Scanner("0\n100\n");
    frontend = new Frontend(scanner, backend);
    frontend.getSongs();
    List<String> songs = backend.getRange(0, 100);
    // Verify that the backend returns songs in the expected range
    assertFalse(songs.isEmpty());
    assertEquals(2, songs.size()); // Assuming placeholder returns 2 songs as asked in
    //piazza and confirmed by TA Mr.Kyle Poage in office hours as backend had some issues
  }

  //addtional edge cases
  @Test
  public void roleTest6() {
    // Simulate user input to get songs with energy range
    Scanner scanner = new Scanner("0\n1\n");
    frontend = new Frontend(scanner, backend);
    frontend.getSongs();
    List<String> songs = backend.getRange(0, 1);
    // Verify that the backend returns songs in the expected range
    assertTrue(songs.isEmpty());
    assertEquals(0, songs.size()); // Assuming placeholder returns 0 songs as no energy
    //is under this radar/threshhold
  }

  /**
   * Tests the setFilter method of the Frontend class. This test checks both applying a danceability
   * filter and clearing it.
   */
  @Test
  public void roleTest3() {
    // Test applying and clearing the filter
    Scanner scanner = new Scanner("30\n");  // Input danceability filter of 30
    frontend = new Frontend(scanner, backend);

    frontend.setFilter();

    List<String> filteredSongs = backend.setFilter(30);
    assertEquals(3, filteredSongs.size());

    scanner = new Scanner("clear\n");  // Input "clear" to remove filter
    frontend = new Frontend(scanner, backend);
    frontend.setFilter();
    filteredSongs = backend.setFilter(null);  // Clear the filter

    assertEquals(3, filteredSongs.size());  // Placeholder still returns 3 songs
  }

  /**
   * Tests the displayTopFive method of the Frontend class. This test checks if the top five most
   * recent songs are displayed correctly.
   */
  @Test
  public void roleTest4() {
    // Test displaying top five most recent songs
    frontend.displayTopFive();
    List<String> topFiveSongs = backend.fiveMost();
    assertEquals(3, topFiveSongs.size());
    assertTrue(topFiveSongs.contains("Cake By The Ocean"));
    assertTrue(topFiveSongs.contains("BO$$"));

  }

  /**
   * Tests the displayMainMenu method of the Frontend class. This test ensures that the main menu is
   * displayed correctly.
   */
  @Test
  public void roleTest5() {

    // Simulated input for loading a song file and then quitting
    String input = "L\nsongs.csv\nQ\n"; // L for loading, followed by a filename and then Q to quit
    TextUITester tester = new TextUITester(input); // Create TextUITester with simulated input

    // Initialize frontend with the simulated input and backend
    Scanner scanner = new Scanner(System.in);
    Frontend frontend = new Frontend(scanner, backend);

    // Run the command loop (which will process the simulated input)
    frontend.runCommandLoop();

    // Capture the output
    String actualOutput = tester.checkOutput();

    assertTrue(actualOutput.contains("Main Menu:"));
  }
  /**
   * Integration Tests Start from Here
   */

  private Backend backend2;

  /**
   * Integration test to verify if the backend successfully reads song data from a CSV file.
   * It ensures that the loaded songs are stored correctly in the backend and can be retrieved.
   *
   * @throws IOException if there's an error reading the file
   */
  @Test
  public void integrationTestReadData() throws IOException {
    // Initialize the backend properly before using it
    IterableSortedCollection<Song> tree = new IterableRedBlackTree<>(); // Use the actual tree implementation
    backend2 = new Backend(tree);  // Initialize backend

    // input file (songs)
    Scanner scanner = new Scanner("songs\n"); //  input file
    frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

    frontend.loadFile(); // Triggers the backend to read the CSV

    List<String> songs = backend2.fiveMost();

    // Perform assertions to verify that the songs were loaded correctly
    assertFalse(songs.isEmpty());
    assertTrue(songs.contains("\"Hello"));
  }
  /**
   * Integration test to verify song retrieval based on energy levels from the backend.
   * The frontend triggers song loading and retrieves a range of songs by energy.
   */
  @Test
  public void integrationTestGetSongsByEnergy() {
    // First block: Retrieve songs with energy range 0-30
    {
      IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
      backend2 = new Backend(tree);
      Scanner scanner = new Scanner("songs\ng\n0\n30\n"); // Simulating input file
      frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

      frontend.loadFile(); // Triggers the backend to read the CSV
      frontend.getSongs();  // energy-based song retrieval

      List<String> songs = backend2.getRange(0, 30);

      assertFalse(songs.isEmpty());
      assertTrue(songs.size() > 0);
      assertTrue(songs.contains("\"All I Ask")); // "All I Ask" is twice in the CSV file
    }

    // Second block: Retrieve songs with energy range 0-20
    {
      IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
      backend2 = new Backend(tree);
      Scanner scanner = new Scanner("songs\ng\n0\n20\n");//Simulating input file
      frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

      frontend.loadFile(); // Triggers the backend to read the CSV
      frontend.getSongs();  // energy-based song retrieval

      List<String> songs = backend2.getRange(0, 20);

      // assertions
     assertFalse(songs.isEmpty());
      assertTrue(songs.size() > 0);
      assertTrue(songs.contains("\"Start")); // song in the CSV file
    }
  }

  /**
   * Integration test for retrieving songs based on danceability.
   * The test verifies that the backend correctly filters songs by danceability.
   */
  @Test
  public void integrationTestGetSongsByDancebility() {
    // Block 1: Danceability filter test
    {
      IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
      backend2 = new Backend(tree);
      Scanner scanner = new Scanner("songs\nf\n40\n"); // Simulating input file
      frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

      frontend.loadFile(); // Triggers the backend to read the CSV
      frontend.setFilter(); // Set danceability filter

      List<String> songs = backend2.setFilter(40); // Retrieve songs matching danceability

      System.out.println("Number of songs matching danceability filter: " + songs.size());

      assertTrue(songs.size() == 555); // Expected number of songs based on the CSV file
    }

    // Block 2: Combined energy and danceability filter test
    {
      IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
      backend2 = new Backend(tree);
      Scanner scanner = new Scanner("songs\ng\n0\n20\nf\n40\n"); // Simulating input file
      frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

      frontend.loadFile(); // Load the song file into the backend
frontend.getSongs();//load get songs to put the energy range
frontend.setFilter();// load dancebility filter
List<String> songs = backend2.setFilter(40);  // Put in the filter
      backend2.getRange(0,20);//specify the energy range

      System.out.println(songs.size());
      assertTrue(songs.size() == 3);// Expected number of songs
      assertTrue(songs.contains("\"Start")); // Song in the CSV file
    }
  }

  /**
   * Integration test for retrieving the top five most recent songs.
   * The test ensures that the backend correctly retrieves the top five songs without filters, and with energy filters.
   */
  @Test
  public void integrationTestGetFiveMost() {
    // First block: Retrieve top five most recent songs without filters
    {
      IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
      backend2 = new Backend(tree);  // Initialize backend

      Scanner scanner = new Scanner("songs\nd\n"); // Simulating input file
      frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

      frontend.loadFile(); // Load the song file into the backend

      List<String> songs = backend2.fiveMost();

      System.out.println("Top 5 most recent songs: " + songs);

      assertTrue(songs.contains("\"Don't Stop the Party (feat. TJR)"));
      assertTrue(songs.contains("\"Written in the Stars (feat. Eric Turner)"));
      assertTrue(songs.contains("\"Hello"));
      assertTrue(songs.contains("\"Rock N Roll"));
      assertTrue(songs.contains("\"Pom Poms"));
    }

    // Second block: Retrieve top five most recent songs with energy filters
    {
      IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
      backend2 = new Backend(tree);  // Initialize backend

      Scanner scanner = new Scanner("songs\nd\n"); // Simulating input file
      frontend = new Frontend(scanner, backend2);  // Initialize frontend with the backend

      frontend.loadFile(); // Load the song file into the backend
      backend2.getRange(0, 10); // Apply energy filter

      List<String> songs = backend2.fiveMost();

      assertTrue(songs.size() == 1); // Expected number of songs
      assertTrue(songs.contains("\"Start")); // Song in the CSV file
    }
  }
}
