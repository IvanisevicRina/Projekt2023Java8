package niti;

import com.example.projekt2023java.PrikazPromjenaController;
import entitet.Promjene;
import entitet.PromjeneManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WriteAChangeThread extends Thread implements Serializable {
    private Promjene<?,?> promjena;

    public WriteAChangeThread(Promjene<?,?> promjena) {
        this.promjena=promjena;
    }


    @Override
    public void run() {
        PromjeneManager promjeneManager = new PromjeneManager();
        promjeneManager.dodajNovuPromjenu(promjena);
        NotificationManager.getInstance().notifyControllers();


    }

}
