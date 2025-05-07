import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** represents the collection of links created by players in the game.
 This class manages the current movies and the established link list.
 And ensure that the rules for the maximum number of links and usage frequency are implemented.
 */

public class Links {
    //List of all links created by the player
    private List<Link> links;
    //the current movie the player is on
    private Movie currentMovie;
    // Array tracking the number of times each type of connectioin has been used
    private int[] Connections;
    // Maximum number of links a player can make
    final int MAX_LINKS = 5;
    //max times a single type of link can be reused
    final int MAX_USED_CONNECTIONS = 3;

    /**
     * Constructs a Links object with default state:
     * - empty link list
     * - no current movie selected
     * - all connection usage counters set to 0
     */

    public Links(){
        links = new ArrayList<Link>();
        currentMovie = null;
        Connections = new int[MAX_LINKS];
    }

    public void addLink(Link link){
        links.add(link);
    }
    public void removeLink(Link link){
        links.remove(link);
    }
    public Link getLink(int index){
        return links.get(index);
    }
}
