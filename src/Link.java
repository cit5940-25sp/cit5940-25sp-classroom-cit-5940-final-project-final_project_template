import java.util.ArrayList;
import java.util.List;

/**
        * Represents a link between two Movie objects.
 * A link is defined by the shared Stuff between the two movies.
        */

public class Link implements Comparable<Link>{
    //The first movie
    private Movie movie1;
    //The second movie
    private Movie movie2;
    //A list of Stuff that is shared bewteen two
    private List<Stuff> share;
    private String connectJob1;
    private String connectJob2;

    public Link(Movie movie 1, Movie movie 2){
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

    public String combinString(String str1, String str2){
        if(str1.compareTo(str2) > 0){
            return str2 + " " + str1;
        }
        else{
            return str1 + " " + str2;
        }
    }
    public int compareTo(Link o){
        String str1 = combinString(connectJob1, connectJob2);
        String str2 = combinString(o.connectJob1, o.connectJob2);
        return str1.compareTo(str2);
    }

 /**
    Check whether the given job is valid.
    Effective jobs are one of the following: "director", "actor", "writer", "composer".
            *
    @param job The task to be checked.
            * @ Return true if the job is valid; otherwise, return false.
            */


   public boolean isValidJob(String job){
       String[] jobs = {"Director", "Actor", "Writer", "composer"};
       for (String j: jobs){
           if(job.equals(j)){
               return true;
           }
       }
        return false
    }
}
