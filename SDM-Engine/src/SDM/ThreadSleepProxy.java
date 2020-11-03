package SDM;

public class ThreadSleepProxy {
    public static void goToSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
