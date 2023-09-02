package entitet;

import java.io.Serializable;
import java.util.List;

public class PromjeneManager implements Serializable {

    public PromjeneManager()  {
    }

    public synchronized void dodajNovuPromjenu(Promjene<?, ?> novaPromjena) {
        List<Promjene<?, ?>> existingPromjene = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
        existingPromjene.add(novaPromjena);
        PromjeneSerijalizer.pisiPromjene(existingPromjene, "dat/promjeneSerijalizirane.ser");

    }


    public synchronized List<Promjene<?, ?>> dohvatiSvePromjene() {
        return PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
    }
    public void izbrisiPromjenu(Promjene<?, ?> promjenaZaBrisanje) {
        // Pretražite listu promjena i uklonite odabranu promjenu
        List<Promjene<?, ?>> listaPromjena = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");

        System.out.println("Promjena za brisanje: " + promjenaZaBrisanje.getOpis());
        System.out.println("----------PRIJE BRISANJA-----------");
        listaPromjena.stream().map(p->p.getOpis()).forEach(System.out::println);

        listaPromjena.remove(promjenaZaBrisanje);
        System.out.println("----------NAKON BRISANJA-----------");
        listaPromjena.stream().map(p->p.getOpis()).forEach(System.out::println);

        // Nakon što izbrišete promjenu, možda ćete htjeti ponovno spremiti promjene u datoteku
        PromjeneSerijalizer.pisiPromjene(listaPromjena, "dat/promjeneSerijalizirane.ser");
    }
}
