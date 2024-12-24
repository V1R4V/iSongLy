import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * This is a placeholder for the fully working Backend that will be developed
 * by one of your teammates this week and then integrated with your role code
 * in a future week. It is designed to help develop and test the functionality
 * of your own Frontend role code this week. Note the limitations described
 * below.
 */
public class Backend_Placeholder implements BackendInterface {

  IterableSortedCollection<Song> tree;

  public Backend_Placeholder(IterableSortedCollection<Song> tree) {
    this.tree = tree;
  }

  public void readData(String filename) throws IOException {
    tree.insert(new Song("DJ Got Us Fallin' In Love (feat. Pitbull)",
        "Usher", "atl hip hop", 2010, 120, 86, 66, -3, 8));
  }

  public List<String> getRange(Integer low, Integer high) {
    String lowString = (char)('A'+low) + " string";
    Song lowSong = new Song(lowString, lowString, lowString,
        low, low, low, low, low, low);
    String highString = (char)('A'+high) + " string";
    Song highSong = new Song(highString, highString, highString,
        high, high, high, high, high, high);
    tree.setIteratorMin(lowSong);
    tree.setIteratorMax(highSong);
    return fiveMost();
  }

  public List<String> setFilter(Integer threshold) {
    return fiveMost();
  }

  public List<String> fiveMost() {
    List<String> titles = new ArrayList<>();
    for(Song song : tree) {
      titles.add(song.getTitle());
    }
    return titles;
  }
}
