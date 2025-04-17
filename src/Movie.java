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
    private List<String> writers;
    private List<String> composers;
    private List<String> cinematographers;

    public Movie(String title, int year, List<String> genres, List<String> actors,
                 List<String> directors, List<String> writers, List<String> composers) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.actors = actors;
        this.directors = directors;
        this.writers = writers;
        this.composers = composers;
        this.cinematographers = cinematographers;
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

    @Override
    public Set<String> getAllContributors() {
        Set<String> contributors = new HashSet<>();
        contributors.addAll(actors);
        contributors.addAll(directors);
        contributors.addAll(writers);
        contributors.addAll(composers);
        contributors.addAll(cinematographers);
        return contributors;
    }
}
