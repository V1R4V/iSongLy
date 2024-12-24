import java.util.Comparator;

public class SongCompare implements Comparator<Song> {

  @Override
  public int compare(Song a, Song b) {
    if(a.getEnergy() < b.getEnergy())
      return -1;
    if(a.getEnergy() > b.getEnergy())
      return 1;
    return 0;
  }
}
