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

    public Links() {
        links = new ArrayList<Link>();
        currentMovie = null;
        Connections = new int[MAX_LINKS];
    }

    // Set the current movie the player is working from
    public void setCurrentMovie(Movie movie) {
        currentMovie = movie;
    }

    // Check if the given link has already been used
// If it has, increment its usage counter
// Return false if the same link has been used more than 3 times

    public boolean addConnection(Link link) {
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).compareTo(link) == 0) {
                Connections[i]++;
                // any specific connection is not allowed to be used
                // more than 3 times
                if (Connections[i] > MAX_USED_CONNECTIONS) {
                    return false;
                }
            }
        }
        return true;
        // Try to create a valid link from the current movie to the given movie
        // Only add it if the connection is allowed and hasn't been overused
        public boolean addLink (Movie movie){
            Link link = new Link(currentMovie, movie);
            if (link.isValidLink() && addConnection(link)) {
                links.add(link);
                currentMovie = movie;
                return true;
            }
            return false;
        }

        // Check if no movie has been set yet
        public boolean isEmpty () {
            if (currentMovie == null) {
                return true;
            }
            return false;
        }

        // Check all the genres from the first movie in each link
        // Then find the common genres shared across them
        // If there’s more than one common genre, return the first one found
        public Genre getCommonGenre () {
            List<Set<Genre>> list = new ArrayList<>();
            for (Link link : links) {
                Movie movie1 = link.getMovie1();
                list.add(new HashSet<Genre>(movie1.getGenres()));
            }

            Set<Genre> commonElements = new HashSet<Genre>(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                commonElements.retainAll(list.get(i));
            }
            Genre commonGenre = null;
            if (commonElements.size() > 1) {
                commonGenre = (Genre) commonElements.toArray()[0];
            }
            return commonGenre;
        }

        public boolean isFull () {
            if (links.size() == MAX_LINKS) {
                return true;
            }
            return false;
        }

        public Movie getCurrentMovie () {
            return currentMovie;
        }

        public Link getLink ( int index){
            return links.get(index);
        }
        @Override
        public String toString () {
            String str = "";
            for (Link link : links) {
                str += link.toString() + "\n";
            }
            return str;
        }
    }
}