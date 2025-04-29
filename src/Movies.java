import java.util.*;

public class Movies {

    private HashMap<String, AbstractMap.Entry<String, HashSet<String>>> allMovies;


    public Movies (String filepath) {
        // load data: create a Movie object for each movie
        // in the file and put it in the HashSet
        // look for name and year and append it in format "Movie Name, (year)"

        // get genre and make a HashSet with all the Staff. Make an AbstractMap.Entry<genre, staff>

        // allMovies.put("Movie Name, (year)", AbstractMap.Entry<genre, staff>)

    }

    /*
    Takes in the name of the previous movie and the current
    that was played and returns the name of the connection
    returns null if there are no connections
     */
    public ArrayList<String> hasConnection (String prev, String curr) {
        return null;
    }

    // add getter genre



}
