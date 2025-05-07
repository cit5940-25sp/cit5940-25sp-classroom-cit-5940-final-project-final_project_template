import java.util.ArrayList;
import java.util.List;

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

    boolean isValidLink(){
        return false
    }
}
