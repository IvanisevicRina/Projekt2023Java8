package entitet;

import java.util.List;

public class PromjeneManager {

    public PromjeneManager() {
    }



    public void spremiPromjenu(Promjene<?, ?> novaPromjena) {

            List<Promjene<?, ?>> existingPromjene = PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");
            existingPromjene.add(novaPromjena);
            System.out.println("Nova promjena"+novaPromjena.getOpis());
            PromjeneSerijalizer.pisiPromjene(existingPromjene, "dat/promjeneSerijalizirane.ser");

    }


    public List<Promjene<?,?>> dohvatiPromjene() {

            return PromjeneSerijalizer.citajPromjene("dat/promjeneSerijalizirane.ser");

    }


}
