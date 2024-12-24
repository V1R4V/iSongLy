import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;

/**
 * Backend
 */
public class Backend implements BackendInterface {

  // list for storing songs that have been through a filter already
  List<Song> songList = new ArrayList<Song>();

  // tree containing the songs
  IterableSortedCollection<Song> tree;

  // comparator for adding songs into tree
  Comparator<Song> compare = new SongCompare();

  /**
   * constructor for backend, intializes with new sorted collection for tree
   * @param tree the new tree for storing songs into
   */
  public Backend(IterableSortedCollection<Song> tree) {
    this.tree = tree;
  }

  /**
   * Loads data from the .csv file referenced by filename.  You can rely
   * on the exact headers found in the provided songs.csv, but you should
   * not rely on them always being presented in this order or on there
   * not being additional columns describing other song qualities.
   * After reading songs from the file, the songs are inserted into
   * the tree passed to the constructor.
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  public void readData(String filename) throws IOException {
    //variables used later in making new song objects (must have placeholder values)
    File songsFile = new File(filename + ".csv");
    String header = "";
    int title = 0;
    int artist = 0;
    int genre = 0;
    int year = 0;
    int bpm = 0;
    int nrgy = 0;
    int dnce = 0;
    int dB = 0;
    int live = 0;
    int headers = 0;
    // reads in the header and associates each header with their position they were mentioned
    try {
      Scanner songsReader = new Scanner(songsFile);

      if(songsReader.hasNextLine()) {
        header = songsReader.nextLine();
        String[] categories = header.split(",");
        headers = categories.length;

        // associates each header with the position they were found in
        for(int i = 0; i <= categories.length-1; i++) {
          if(categories[i].equals("title")) { title = i; }
          if(categories[i].equals("artist")) { artist = i; }
          if(categories[i].equals("genre")) { genre = i; }
          if(categories[i].equals("year")) { year = i; }
          if(categories[i].equals("bpm")) { bpm = i; }
          if(categories[i].equals("nrgy")) { nrgy = i; }
          if(categories[i].equals("dnce")) { dnce = i; }
          if(categories[i].equals("dB")) { dB = i; }
          if(categories[i].equals("live")) { live = i; }
        }
      }

      // reads in the songs from the file and associates each section with one of
      // the header sections defined in the above chunk of code
      while(songsReader.hasNextLine()) {
        String currSong = songsReader.nextLine();
        String[] currSongData = currSong.split(",");

        // if data mismatches headers, skip song
        if(currSongData.length > headers)
          continue;
        // constructs new song given the data provided
        Song newSong = new Song(currSongData[title],
            currSongData[artist],
            currSongData[genre],
            Integer.parseInt(currSongData[year]),
            Integer.parseInt(currSongData[bpm]),
            Integer.parseInt(currSongData[nrgy]),
            Integer.parseInt(currSongData[dnce]),
            Integer.parseInt(currSongData[dB]),
            Integer.parseInt(currSongData[live]),
            compare);
        tree.insert(newSong);
      }
      songsReader.close();
    }

    // if there is no file matching input, throw an IOException
    catch (FileNotFoundException e) {
      throw new IOException("File named was not found");
    }
  }

  /**
   * Retrieves a list of song titles from the tree passed to the contructor.
   * The songs should be ordered by the songs' Energy, and that fall within
   * the specified range of Energy values.  This Energy range will
   * also be used by future calls to the setFilter and getmost Recent methods.
   *
   * If a Danceability filter has been set using the setFilter method
   * below, then only songs that pass that filter should be included in the
   * list of titles returned by this method.
   *
   * When null is passed as either the low or high argument to this method,
   * that end of the range is understood to be unbounded.  For example, a null
   * high argument means that there is no maximum Energy to include in
   * the returned list.
   *
   * @param low is the minimum Energy of songs in the returned list
   * @param high is the maximum Energy of songs in the returned list
   * @return List of titles for all songs from low to high, or an empty
   *     list when no such songs have been loaded
   */
  public List<String> getRange(Integer low, Integer high) {
    // sets up variables and clears current range
    List<String> songTitleList = new ArrayList<String>();
    songList.clear();
    Iterator<Song> songFinder = tree.iterator();

    // if no low or high provided, make it as big/little as possible
    if(low == null) { low = Integer.MIN_VALUE; }
    if(high == null) { high = Integer.MAX_VALUE; }

    // while their are songs that fit range, add them to songList
    while(songFinder.hasNext()) {
      Song next = songFinder.next();
      if(next.getEnergy() < high && next.getEnergy() > low) {
        songList.add(next);
      }
    }

    // order the songs gathered above from low to high energy
    // (this is using selection sort)
    for(int i = 0; i < songList.size(); i++) {
      for(int g = i+1; g < songList.size(); g++) {
        if(songList.get(g).compareTo(songList.get(i)) > 0) {
          Song temp = songList.get(i);
          songList.set(i,songList.get(g));
          songList.set(g,temp);
        }
      }
    }

    // add the titles from songList to a list to return all title names
    for(int y = 0; y < songList.size(); y++) {
      songTitleList.add(songList.get(y).getTitle());
    }

    return songTitleList;
  }

  /**
   * Retrieves a list of song titles that have a Danceability that is
   * larger than the specified threshold.  Similar to the getRange
   * method: this list of song titles should be ordered by the songs'
   * Energy, and should only include songs that fall within the specified
   * range of Energy values that was established by the most recent call
   * to getRange.  If getRange has not previously been called, then no low
   * or high Energy bound should be used.  The filter set by this method
   * will be used by future calls to the getRange and getmost Recent methods.
   *
   * When null is passed as the threshold to this method, then no Danceability
   * threshold should be used.  This effectively clears the filter.
   *
   * @param threshold filters returned song titles to only include songs that
   *     have a Danceability that is larger than this threshold.
   * @return List of titles for songs that meet this filter requirement, or
   *     an empty list when no such songs have been loaded
   */
  public List<String> setFilter(Integer threshold) {
    // set up variables used in method
    List<String> danceSongs = new ArrayList<String>();
    List<Song> tempSongList = new ArrayList<Song>(songList);
    Iterator<Song> songFinder;

    // if no threshold set, make it the lowest value possible
    if(threshold == null)
      threshold = Integer.MIN_VALUE;

    // if no range is set, just iterate through tree
    if(songList.isEmpty())
      songFinder = tree.iterator();
      // if range was set, create a temp list to iterate through and clear the current songList
    else {
      songFinder = tempSongList.iterator();
      songList.clear();
    }

    // while there are songs that pass the threshold, add them to songList
    while(songFinder.hasNext()) {
      Song next = songFinder.next();
      if(next.getDanceability() > threshold) {
        songList.add(next);
        danceSongs.add(next.getTitle());
      }
    }

    return danceSongs;
  }

  /**
   * This method returns a list of song titles representing the five
   * most Recent songs that both fall within any attribute range specified
   * by the most recent call to getRange, and conform to any filter set by
   * the most recent call to setFilter.  The order of the song titles in this
   * returned list is up to you.
   *
   * If fewer than five such songs exist, return all of them.  And return an
   * empty list when there are no such songs.
   *
   * @return List of five most Recent song titles
   */
  public List<String> fiveMost() {
    List<String> topFive = new ArrayList<String>();

    // if no other method has been called to sort yet, call getRange unbounded
    if(songList.isEmpty())
      getRange(null, null);

    // if there are less than 5 values, just add all of them in high to low order
    if(songList.size() < 5) {
      for(int i = songList.size() - 1; i >= 0; i--)
        topFive.add(songList.get(i).getTitle());
      // if there are more than five values, only add the top 5 (high to low still)
    } else {
      for(int i = 4; i >= 0; i--)
        topFive.add(songList.get(i).getTitle());
    }

    return topFive;
  }

}
