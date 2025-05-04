public class CountdownTimer {
    private final long durationMillis;
    private long startTime;
    private boolean running;

    public CountdownTimer(long durationSeconds) {
        this.durationMillis = durationSeconds * 1000;
        this.running = false;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public boolean isRunning() {
        if (!running) return false;
        long now = System.currentTimeMillis();
        return (now - startTime) < durationMillis;
    }

    public long getTimeLeftSeconds() {
        if (!running) return 0;
        long now = System.currentTimeMillis();
        long timeLeft = durationMillis - (now - startTime);
        return Math.max(0, timeLeft / 1000);
    }
}

