import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a link between two Movie objects.
 * A link is defined by the shared Stuff between the two movies.
 */
public class Link implements Comparable<Link>{
    // The first movie in the link.
    private Movie movie1;
    // The second movie in the link.
    private Movie movie2;
    // A list of Stuff that is shared between movie1 and movie2.
    private List<Map.Entry<Stuff, Stuff>> connections;
    private int shareIndex;

    /**
     * Constructs a new Link object between two movies and calculates the shared Stuff.
     *
     * @param movie1 the first movie.
     * @param movie2 the second movie.
     */
    public Link(Movie movie1, Movie movie2){
        // Initialize the first movie.
        this.movie1 = movie1;
        // Initialize the second movie.
        this.movie2 = movie2;
        // Initialize the list of shared Stuff.
        connections = new ArrayList<>();
        // Calculate the shared Stuff between the two movies.
        calculteShare(); // Note: There's a typo in the method name "calculteShare", should be "calculateShare"
    }

    public Movie getMovie1() {
        return movie1;
    }

    public String combinString(String str1, String str2){
        if(str1.compareTo(str2) > 0){
            return str2 + " " + str1;
        }
        else{
            return str1 + " " + str2;
        }
    }


    public int compareTo(Link o){
        int cmp = movie1.compareTo(o.movie1);
        if(cmp != 0){
            return cmp;
        }
        return movie2.compareTo(o.movie2);
    }

    public String getShareString(){
        String shareString = null;
        if(connections.size() > 0 && ++shareIndex < connections.size()){

            Map.Entry<Stuff, Stuff> pair = connections.get(shareIndex);
            String connectJob1 = pair.getKey().getJob();
            String connectJob2 = pair.getValue().getJob();
            shareString = combinString(connectJob1, connectJob2);
        }
        return shareString;
    }

    /**
     * Checks if the given job is valid.
     * A valid job is one of the following: "Director", "Actor", "Writer", "composer".
     *
     * @param job the job to check.
     * @return true if the job is valid, false otherwise.
     */
    public boolean isValidJob(String job){
        String[] jobs = {"Director", "Actor", "Writer", "Original Music Composer"};
        for(String j : jobs){
            if(job.equals(j)){
                return true;
            }
        }
        return false;
    }
    /**
     * Calculates the list of Stuff that is shared between movie1 and movie2.
     */
    public void calculteShare(){
        shareIndex = -1;
        // Iterate through each Stuff in movie1.
        for(Stuff stuff : movie1.getStuffs()){
            if(!movie2.getStuffs().contains(stuff)){
                continue;
            }
            // Check if movie2 also contains the current Stuff.
            Stuff other = movie2.getStuffs().floor(stuff);
            String job1 = stuff.getJob();
            String job2 = other.getJob();
            // If both movies contain the same Stuff, add it to the shared list.
            if(stuff.compareTo(other) == 0 && isValidJob(job1) && isValidJob(job2)){
                connections.add(new AbstractMap.SimpleEntry<Stuff, Stuff>(stuff, other) );
            }
        }
    }


    /**
     * Returns a string representation of the Link object.
     *
     * @return a string describing the two movies and their shared Stuff (if any).
     */
    @Override
    public String toString() {
        // If there is no shared Stuff, return an empty string.
        if(connections.size() == 0){
            return "";
        }
        Map.Entry<Stuff, Stuff> pair = connections.get(shareIndex);
        // Initialize the string with the names of the two movies.
        String str = movie1.toString() + " with " + pair.getKey().toString();
        str += "\n" + movie2.toString() + " with " + pair.getValue().toString();
        return str;
    }
}

