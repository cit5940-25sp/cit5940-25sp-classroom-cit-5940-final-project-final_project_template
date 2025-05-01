package strategy;

import model.Movie;
import model.Person;

/**
 * Link strategy where two movies are considered connected if they share at least one actor.
 */
public class ActorLinkStrategy implements ILinkStrategy {

    @Override
    public boolean isValidLink(Movie from, Movie to) {
        for (Person actor1 : from.getActors()) {
            for (Person actor2 : to.getActors()) {
                if (actor1.getName().equals(actor2.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getReason(Movie from, Movie to) {
        for (Person actor1 : from.getActors()) {
            for (Person actor2 : to.getActors()) {
                if (actor1.getName().equals(actor2.getName())) {
                    return "Shared actor: " + actor1.getName();
                }
            }
        }
        return "No shared actors";
    }
}
