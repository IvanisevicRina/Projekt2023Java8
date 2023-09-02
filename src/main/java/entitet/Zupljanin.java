package entitet;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Zupljanin extends Osoba  implements Serializable {

    private String sifra;
    private LocalDate datumRodjenja;
    private List<Slike> slike;



    public Zupljanin(Long id, String ime, String prezime, String sifra, LocalDate datumRodjenja) {
        super(id, ime, prezime);
        this.sifra = sifra;
        this.datumRodjenja = datumRodjenja;
        slike=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Zupljanin{" +
                "ime=" + getIme() +
                "prezime="+getPrezime()+
                "sifra='" + sifra + '\'' +
                ", datumRodjenja=" + datumRodjenja +
                '}';
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public List<Slike> getSlike() {
        return slike;
    }

    public void setSlike(List<Slike> slike) {
        this.slike = slike;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

}
