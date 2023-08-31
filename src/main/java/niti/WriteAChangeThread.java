package niti;

import entitet.Promjene;
import entitet.PromjeneManager;

import java.util.ArrayList;
import java.util.List;

public class WriteAChangeThread extends Thread {
    private Promjene<?,?> promjena;
    public WriteAChangeThread(Promjene<?,?> promjena) {
        this.promjena=promjena;
    }


    @Override
    public void run() {
        PromjeneManager promjeneManager = new PromjeneManager();
        promjeneManager.dodajNovuPromjenu(promjena);

    }

}
