import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Stores info per movie (model)
public class Movie implements IMovie {
    private String title;
    private int year;
    private List<String> genres;
    private List<String> actors;
    private List<String> directors;
    private List<String> crew;
    private List<String> writers;
    private List<String> composers;
    private List<String> cinematographers;
    private Set<String> contributors;

    public Movie(String title, int year, List<String> genres) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.actors = new ArrayList<>();
        this.directors = new ArrayList<>();
        this.crew = new ArrayList<>();
        this.writers = new ArrayList<>();
        this.composers = new ArrayList<>();
        this.cinematographers = new ArrayList<>();
        this.contributors = new HashSet<>();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public List<String> getGenres() {
        return genres;
    }

    @Override
    public List<String> getActors() {
        return actors;
    }

    public List<String> getCrew() {
        return crew;
    }

    public void addActor(String name) {
        actors.add(name);
        contributors.add(name);
    }

    public void addCrew(String name) {
        crew.add(name);
        contributors.add(name);
    }

    public void addDirector(String name) {
        directors.add(name);
        contributors.add(name);
    }

    public void addWriter(String name) {
        writers.add(name);
        contributors.add(name);
    }

    public void addComposer(String name) {
        composers.add(name);
        contributors.add(name);
    }

    public void addCinematographer(String name) {
        cinematographers.add(name);
        contributors.add(name);
    }

    @Override
    public List<String> getDirectors() {
        return directors;
    }

    @Override
    public List<String> getWriters() {
        return writers;
    }

    @Override
    public List<String> getComposers() {
        return composers;
    }

    @Override
    public List<String> getCinematographers() {
        return cinematographers;
    }

    public void addContributor(String name) {
        contributors.add(name);
    }
    @Override
    public Set<String> getAllContributors() {
        return contributors;
    }
    @Override
    public String toString() {
        return getTitle() + " | Genres: " + getGenres() + " | Contributors: " + getAllContributors();
    }
}
