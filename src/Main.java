public class Main {
    public static void main(String[] args) {
        TMDBClient client = new TMDBClient();

        Movie movie = client.fetchMovieByTitle("Inception");
        if (movie != null) {
            System.out.println("Connected！movie name：" + movie.getTitle());
            System.out.println("year of movie：" + movie.getYear());
            System.out.println("director：" + movie.getDirectors());
            System.out.println("actor：" + movie.getActors());
            System.out.println("genre：" + movie.getGenres());
        } else {
            System.out.println("failed");
        }
    }
}
