package niti;

import com.example.projekt2023java.PrikazPromjenaController;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static NotificationManager instance = new NotificationManager();
    private List<PrikazPromjenaController> registeredControllers = new ArrayList<>();
    private boolean changesOccurred = false;

    private NotificationManager() {
    }

    public static NotificationManager getInstance() {
        return instance;
    }

    public synchronized void registerController(PrikazPromjenaController controller) {
        registeredControllers.add(controller);
    }

    public synchronized void notifyControllers() {
        changesOccurred = true;
        notifyAll();
    }

    public synchronized void waitForNotification() throws InterruptedException {
        while (!changesOccurred) {
            wait();
        }
        changesOccurred = false;
    }
}
