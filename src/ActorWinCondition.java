public class ActorWinCondition implements WinCondition {
    private String actor;

    public ActorWinCondition(String actor) {
        this.actor = actor;
    }

    @Override
    public boolean checkWin(Player player) {
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getActors().contains(actor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Has a movie with actor: " + actor;
    }
}
