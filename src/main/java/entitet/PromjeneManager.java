package entitet;

import java.util.List;

public class PromjeneManager {
    public boolean promjenaZavrsena = true;


    public PromjeneManager() {
    }



    public synchronized void spremiPromjenu(Promjene<?, ?> novaPromjena) {
        promjenaZavrsena = false;
        System.out.println("Sada ću pisati u serFile");
        List<Promjene<?, ?>> existingPromjene = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
        existingPromjene.add(novaPromjena);
        System.out.println("Nova promjena" + novaPromjena.getOpis());
        PromjeneSerijalizer.pisiPromjene(existingPromjene, "dat/promjeneSerijalizirane.ser");

        try {
            Thread.sleep(30000); // Pauza od 30 sekundi (30000 milisekundi)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Gotov sam, više ne trebam pisati, vraćam ti na true;");
        promjenaZavrsena = true;
        notify(); // Obavijesti drugu nit da je promjena završena
    }

    public synchronized void dodajNovuPromjenu(Promjene<?, ?> novaPromjena) {
        List<Promjene<?, ?>> existingPromjene = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
        existingPromjene.add(novaPromjena);
        PromjeneSerijalizer.pisiPromjene(existingPromjene, "dat/promjeneSerijalizirane.ser");

        // Obavijesti sve niti koje čekaju na promjene
        notifyAll();
    }


    public List<Promjene<?, ?>> dohvatiSvePromjene() {
        return PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
    }

}
