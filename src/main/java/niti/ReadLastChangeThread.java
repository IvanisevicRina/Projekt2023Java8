package niti;

import entitet.Promjene;
import entitet.PromjeneManager;

import java.util.ArrayList;
import java.util.List;

public class ReadLastChangeThread extends Thread {
    private static final int INTERVAL_SECONDS = 10;
    private final PromjeneManager promjeneManager = new PromjeneManager();


    @Override
    public void run() {
        while (true) {

            List<Promjene<?,?>> listaPromjena = promjeneManager.dohvatiSvePromjene();
            listaPromjena.forEach(System.out::println);

            try {
                Thread.sleep(INTERVAL_SECONDS * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
