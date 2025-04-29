package strategy;

import model.Movie;

/**
 * Link strategy where two movies are connected if they share a common director.
 */
public class DirectorLinkStrategy implements ILinkStrategy {

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
