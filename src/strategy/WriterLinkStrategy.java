package strategy;

import model.Movie;
import model.Person;
import model.PersonRole;
import model.strategy.LinkStrategy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Strategy that validates two movies share at least one writer.
 */
public class WriterLinkStrategy implements LinkStrategy {
    @Override
    public boolean isValidLink(Movie from, Movie to) {
        return !Collections.disjoint(
                from.getWriters(),
                to.getWriters()
        );
    }

    @Override
    public String getReason(Movie from, Movie to) {
        Set<Person> common = from.getWriters().stream()
                .filter(to.getWriters()::contains)
                .collect(Collectors.toSet());
        String name = common.iterator().next().getName();
        return "Shared writer: " + name;
    }
}