package entitet;

import java.io.Serializable;
import java.util.List;

public class PromjeneManager implements Serializable {

    public PromjeneManager()  {
    }

    public void dodajNovuPromjenu(Promjene<?, ?> novaPromjena) {
        List<Promjene<?, ?>> existingPromjene = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
        existingPromjene.add(novaPromjena);
        PromjeneSerijalizer.pisiPromjene(existingPromjene, "dat/promjeneSerijalizirane.ser");

    }


    public List<Promjene<?, ?>> dohvatiSvePromjene() {
        return PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
    }
    public void izbrisiPromjenu(Promjene<?, ?> promjenaZaBrisanje) {

        List<Promjene<?, ?>> listaPromjena = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");

        listaPromjena.remove(promjenaZaBrisanje);

        PromjeneSerijalizer.pisiPromjene(listaPromjena, "dat/promjeneSerijalizirane.ser");
    }
}
