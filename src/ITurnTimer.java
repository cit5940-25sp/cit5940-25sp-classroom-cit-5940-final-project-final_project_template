public interface ITurnTimer {

    /*
    starts the countdown for the current player's turn
    */
    public void start(int seconds, Runnable onTimeout);

    /*
    when player makes a move in time
    stops the timer, resets it
     */
    public void cancel();

    /*
    returns the remaining time in seconds
     */
    public int getRemainingTime();

}
