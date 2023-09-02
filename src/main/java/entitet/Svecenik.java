package entitet;

import baza.BazaPodataka;
import iznimke.DuplikatSifreException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Svecenik extends Osoba  implements Serializable, SifraProvjerljiv {
    private static final long serialVersionUID = -4717525006694345544L;


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

    @Override
    public void provjeraSifre(String sifra) throws DuplikatSifreException {
        List<Svecenik> svecenici = BazaPodataka.dohvatiSveSvecenike();
        for (Svecenik svecenik : svecenici) {
            if (svecenik.getSifra().equals(sifra)) {
                throw new DuplikatSifreException("Svecenik sa istom šifrom već postoji!");
            }
        }
    }
}
