package entitet;

import java.util.concurrent.Semaphore;

public class SemaphoreManager {
    private static final Semaphore semaphore = new Semaphore(1);

    public static void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public static void release() {
        semaphore.release();
    }
}
