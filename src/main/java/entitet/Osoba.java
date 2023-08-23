package entitet;

import java.io.Serializable;

public abstract class Osoba extends Entitet implements Serializable {

    private String ime;

    private String prezime;


    public Osoba(Long id, String ime, String prezime) {
        super(id);
        this.ime = ime;
        this.prezime = prezime;
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                '}';
    }


    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }


}
