package entitet;

import java.io.Serializable;
import java.util.Date;

public class Promjene implements Serializable {
    private String korisnik;
    private String opisPromjene;
    private Date vrijemePromjene;

    public Promjene(String korisnik, String opisPromjene, Date vrijemePromjene) {
        this.korisnik = korisnik;
        this.opisPromjene = opisPromjene;
        this.vrijemePromjene = vrijemePromjene;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public String getOpisPromjene() {
        return opisPromjene;
    }

    public Date getVrijemePromjene() {
        return vrijemePromjene;
    }
}





