import java.util.ArrayList;
import java.util.HashSet;
import java.util.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Links {
    private List<Link> links;
    private Movie currentMovie;
    private Map<String, Integer> connections;
    // Maximum number of links that can be stored in the Links object
    final int MAX_LINKS = 5;
    // Maximum number of times a specific connection can be used
    final int MAX_USED_CONNECTIONS = 3;

    /**
     * Constructs a new Links object.
     * Initializes the list of links, sets the current movie to null,
     * and initializes the connections map.
     */
    public Links(){
        // Initialize the list of links as an ArrayList
        links = new ArrayList<Link>();
        // Set the current movie to null
        currentMovie = null;
        // Initialize the connections map as a Hashtable
        connections = new Hashtable<>();
    }

    /**
     * Sets the current movie.
     *
     * @param movie the movie to be set as the current movie
     */
    public void setCurrentMovie(Movie movie){
        // Assign the provided movie to the currentMovie field
        currentMovie = movie;
    }

    /**
     * Tries to establish a connection using the provided link.
     * If the share string of the link has been used less than the maximum allowed times,
     * the connection count is incremented, and the method returns true.
     * Otherwise, it keeps trying until a valid connection is found or returns false.
     *
     * @param link the link to use for establishing the connection
     * @return true if the connection is successfully established, false otherwise
     */
    public boolean tryConnection(Link link){
        // Keep trying until a valid connection is found or the loop breaks
        while (true) {
            // Get the share string from the link
            String shareString = link.getShareString();
            // If the share string is null, return false as no valid connection can be made
            if(shareString == null){
                return false;
            }
            // Check if the share string is already in the connections map and has reached the maximum usage
            if(connections.containsKey(shareString) &&
                    connections.get(shareString) >= MAX_USED_CONNECTIONS){
                // If so, skip to the next iteration and try again
                continue;
            }else{
                // Otherwise, increment the usage count of the share string in the connections map
                connections.put(shareString, connections.getOrDefault(shareString, 0) + 1);
                // Break the loop as a valid connection has been established
                break;
            }
        }
        // Return true as the connection was successfully established
        return true;
    }

    /**
     * Attempts to add a new link with the provided movie.
     * If the connection can be established, the link is added to the list,
     * and the current movie is updated.
     *
     * @param movie the movie to create a link with
     * @return true if the link is successfully added, false otherwise
     */
    public boolean addLink(Movie movie){
        // Create a new link between the current movie and the provided movie
        Link link = new Link(currentMovie, movie);
        // Try to establish a connection using the created link
        if(tryConnection(link)){
            // If the connection is successful, add the link to the list
            links.add(link);
            // Update the current movie to the provided movie
            currentMovie = movie;
            // Return true as the link was successfully added
            return true;
        }
        // Return false if the link could not be added
        return false;
    }

    /**
     * Checks if the Links object is empty.
     * A Links object is considered empty if the current movie is null.
     *
     * @return true if the current movie is null, false otherwise
     */
    public boolean isEmpty(){
        // Check if the current movie is null
        if(currentMovie == null){
            return true;
        }
        return false;
    }

    /**
     * Retrieves the common genre among all movies in the links.
     * If there are multiple common genres, it returns the first one in the set.
     * If there are no common genres, it returns null.
     *
     * @return the common genre among all movies in the links, or null if none exists.
     */
    public Genre getCommonGenre(){
        // Create a list to store sets of genres for each movie in the links
        List<Set<Genre>> list = new ArrayList<>();
        // Iterate through each link in the links list
        for(Link link : links){
            // Get the first movie from the current link
            Movie movie1 = link.getMovie1();
            // Add a set of genres of the current movie to the list
            list.add(new HashSet<Genre>(movie1.getGenres()));
        }

        // Initialize a set of common elements with the genres of the first movie
        Set<Genre> commonElements = new HashSet<Genre>(list.get(0));
        // Iterate through the remaining sets of genres in the list
        for(int i = 1; i < list.size(); i++){
            // Retain only the elements that are present in both commonElements and the current set
            commonElements.retainAll(list.get(i));
        }
        // Initialize the common genre variable to null
        Genre commonGenre = null;
        // Check if there is at least one common genre
        if(commonElements.size() > 0){
            // Get the first common genre from the set
            commonGenre = (Genre) commonElements.toArray()[0];
        }
        // Return the common genre, or null if none exists
        return commonGenre;
    }

    public boolean isFull(){
        if(links.size() == MAX_LINKS){
            return true;
        }
        return false;
    }
    public Movie getCurrentMovie() {
        return currentMovie;
    }
    public Link getLink(int index){
        return links.get(index);
    }
    @Override
    public String toString() {
        String str = "links: \n";
        int i = 1;
        for(Link link : links){
            str += "link"+ i + ":\n"+ link.toString() + "\n";
            i++;
        }
        return str;
    }
}