import java.util.ArrayList;
import java.util.List;

// Handles indexing and searching movies (uses map) (model)
public class MovieIndex implements IMovieIndex {
    private List<IMovie> movieList;

    public MovieIndex() {
        this.movieList = new ArrayList<>();
    }

    @Override
    public void loadMovies(List<IMovie> movieList) {
        if (movieList == null) {
            this.movieList = new ArrayList<>();
        } else {
            this.movieList = movieList;
        }
    }

    @Override
    public IMovie getMovieByTitle(String title) {
        return null;
    }

    @Override
    public List<String> autocomplete(String input) {
        return List.of();
    }

    @Override
    public List<IMovie> getConnectedMovies(IMovie movie) {
        return List.of();
    }

    @Override
    public boolean containsMovie(String title) {
        return false;
    }
}
