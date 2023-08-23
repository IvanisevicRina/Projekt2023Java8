package entitet;

import java.io.Serializable;

public class Slike implements Serializable {
    private String naziv;
    private byte[] slika;

    public Slike(String naziv, byte[] slika) {
        this.naziv = naziv;
        this.slika = slika;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public byte[] getSlika() {
        return slika;
    }

    public void setSlika(byte[] slika) {
        this.slika = slika;
    }



}
