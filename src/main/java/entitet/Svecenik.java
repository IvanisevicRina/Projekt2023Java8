package entitet;

import java.io.Serializable;
import java.time.LocalDate;

public class Svecenik extends Osoba  implements Serializable {

    private String sifra;
    private String titula;
    private LocalDate datumRodjenja;


    public Svecenik(Long id, String ime, String prezime, String sifra, String titula, LocalDate datumRodjenja) {
        super(id, ime, prezime);
        this.sifra = sifra;
        this.titula = titula;
        this.datumRodjenja = datumRodjenja;
    }

    public static SvecenikBuilder builder() {
        return new SvecenikBuilder();
    }


    @Override
    public String toString() {
        return "Svecenik{" +
                "ime=" + getIme() +
                "prezime="+getPrezime()+
                "sifra='" + sifra + '\'' +
                ", titula='" + titula + '\'' +
                '}';
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getTitula() {
        return titula;
    }

    public void setTitula(String titula) {
        this.titula = titula;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }
}
