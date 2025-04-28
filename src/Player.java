public class Player {
    private String name;
    private IWinStrategy winStrategy;
    private int progress;

    public void updateProgress(Movie movie) {
        winStrategy.updateProgress(movie);
    }

    public String getName() {
        return name;
    }

    public IWinStrategy getWinStrategy() {
        return winStrategy;
    }

    public int getProgress() {
        return progress;
    }
}
