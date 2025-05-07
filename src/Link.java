import java.util.ArrayList;
import java.util.List;

/**
        * Represents a link between two Movie objects.
 * A link is defined by the shared Stuff between the two movies.
        */

public class Link implements Comparable<Link> {
    //The first movie
    private Movie movie1;
    //The second movie
    private Movie movie2;
    //A list of Stuff that is shared bewteen two
    private List<Stuff> share;
    private String connectJob1;
    private String connectJob2;

    public Link(Movie movie 1, Movie movie 2) {
        //initialize the first movie
        this.movie1 = movie1;
        // initialize the second
        this.movie2 = movie2;
        //initialize the list
        share = new ArrayList<>();
        //Calculate
        calculateShare();
    }

    public Movie getMovie1() {
        return movie1;
    }

    public String combinString(String str1, String str2) {
        if (str1.compareTo(str2) > 0) {
            return str2 + " " + str1;
        } else {
            return str1 + " " + str2;
        }
    }

    public int compareTo(Link o) {
        String str1 = combinString(connectJob1, connectJob2);
        String str2 = combinString(o.connectJob1, o.connectJob2);
        return str1.compareTo(str2);
    }

    /**
     * Check whether the given job is valid.
     * Effective jobs are one of the following: "director", "actor", "writer", "composer".
     *
     * @param job The task to be checked.
     * @ Return true if the job is valid; otherwise, return false.
     */


    public boolean isValidJob(String job) {
        String[] jobs = {"Director", "Actor", "Writer", "composer"};
        for (String j : jobs) {
            if (job.equals(j)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates if the list of Stuff that is shared between movie1 and movie2.
     */

    public void calculateShare() {
        // Iterate through each Stuff in movie1.
        for (Stuff stuff : movie1.getStuffs()) {
            // Check if movie2 also contains the current Stuff.
            Stuff other = movie2.getStuffs().floor(stuff);
            String job1 = stuff.getJob();
            String job2 = other.getJob();
            // If both movies contain the same Stuff, add it to the shared list.
            if (stuff.compareTo(other) == 0 && isValidJob(job1) && isValidJob(job2)) {
                share.add(stuff);
                connectJob1 = job1;
                connectJob2 = job2;
                break;
            }
        }
    }

    /**
     * Check if the link is valid between two movies
     * It will be valid if there is at least one shared stuff
     *
     * @return true if there is at least one, false otherwise.
     */
    boolean isValidLink() {
        // return T if not empty
        return share.size() > 0;
    }

    /**
     * Returns a string representation of the Link object.
     *
     * @return a string describing the two movies and their shared Stuff (if any).
     */
    @Override
    public String toString() {
        // If there is no shared Stuff, return an empty string.
        if (!isValidLink()) {
            return "";
        }
        Stuff stuff = share.get(0);
        // Initialize the string with the names of the two movies.
        String str = movie1.toString() + " with " + stuff.toString();
        str += "\n" + movie2.toString() + " with " + stuff.toString();
        return str;
    }
}
