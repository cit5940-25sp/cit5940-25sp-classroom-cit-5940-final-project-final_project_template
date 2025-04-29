package strategy;

import model.Movie;

/**
 * Link strategy where two movies are considered connected if they share at least one actor.
 */
public class ActorLinkStrategy implements ILinkStrategy {

    @Override
    public boolean isValidLink(Movie from, Movie to) {
        // TODO
        return false;
    }

    @Override
    public String getReason(Movie from, Movie to) {
        // TODO
        return null;
    }
}
