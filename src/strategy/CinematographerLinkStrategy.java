package strategy;

import model.Movie;
import model.Person;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Strategy that validates two movies share at least one cinematographer.
 */
public class CinematographerLinkStrategy implements ILinkStrategy {
    @Override
    public boolean isValidLink(Movie from, Movie to) {
        return !Collections.disjoint(
                from.getCinematographers(),
                to.getCinematographers()
        );
    }

    @Override
    public String getReason(Movie from, Movie to) {
        Set<Person> common = from.getCinematographers().stream()
                .filter(to.getCinematographers()::contains)
                .collect(Collectors.toSet());
        String name = common.iterator().next().getName();
        return "Shared cinematographer: " + name;
    }
}
