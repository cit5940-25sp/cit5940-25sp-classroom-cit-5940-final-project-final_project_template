import java.util.*;

// Handles indexing and searching movies (uses map) (model)
public class MovieIndex implements IMovieIndex {
    private Map<String, IMovie> indexByTitle = new HashMap<>();
    private Map<String, Set<IMovie>> indexByContributor = new HashMap<>();


    @Override
    public void loadMovies(List<IMovie> movieList) {
        for (IMovie movie : movieList) {
            indexByTitle.put(movie.getTitle().toLowerCase(), movie);
            for (String person : movie.getAllContributors()) {
                if (!indexByContributor.containsKey(person.toLowerCase())) {
                    indexByContributor.put(person.toLowerCase(), new HashSet<>());
                } else {
                    indexByContributor.get(person).add(movie);
                }
            }
        }
    }

    @Override
    public IMovie getMovieByTitle(String title) {
        return indexByTitle.get(title.toLowerCase());
    }

    @Override
    public List<String> autocomplete(String input) {
        String inputLowerCase = input.toLowerCase();
        List<String> results = new ArrayList<>();

        for (String title : indexByTitle.keySet()) {
            if (title.startsWith(inputLowerCase)) {
                results.add(indexByTitle.get(title).getTitle());
            }
        }
        return results;
    }

    @Override
    public List<IMovie> getConnectedMovies(IMovie movie) {
        Set<IMovie> connectedMovies = new HashSet<>();

        for (String person : movie.getAllContributors()) {
            String name = person.toLowerCase();

            if (indexByContributor.containsKey(name)) {
                connectedMovies.addAll(indexByContributor.get(name));
            }
        }

        connectedMovies.remove(movie);
        return new ArrayList<>(connectedMovies);
    }

    @Override
    public boolean containsMovie(String title) {
        return indexByTitle.containsKey(title.toLowerCase());
    }
}
