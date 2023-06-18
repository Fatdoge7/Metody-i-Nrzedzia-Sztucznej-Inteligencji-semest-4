import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant startTime;
    private Instant stopTime;

    public void start() {
        startTime = Instant.now();
    }

    public void stop() {
        stopTime = Instant.now();
    }

    public void checkTimer() {
        Instant currentTime = Instant.now();
        Duration duration;

        if (startTime != null && stopTime != null) {
            duration = Duration.between(startTime, stopTime);
        } else if (startTime != null) {
            duration = Duration.between(startTime, currentTime);
        } else {
            System.out.println("Timer has not been started yet.");
            return;
        }

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        System.out.println("Program running time: " + minutes + " minutes " + remainingSeconds + " seconds.");
    }
}