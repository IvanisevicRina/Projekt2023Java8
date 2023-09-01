package entitet;

import java.util.List;

public class PromjeneManager {

    public PromjeneManager() {
    }

    public synchronized void dodajNovuPromjenu(Promjene<?, ?> novaPromjena) {
        List<Promjene<?, ?>> existingPromjene = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
        existingPromjene.add(novaPromjena);
        PromjeneSerijalizer.pisiPromjene(existingPromjene, "dat/promjeneSerijalizirane.ser");

    }


    public synchronized List<Promjene<?, ?>> dohvatiSvePromjene() {
        return PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
    }

}
