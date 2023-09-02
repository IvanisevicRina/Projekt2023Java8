package entitet;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ZupljaninBuilder implements Serializable {
    private Long id;
    private String ime;
    private String prezime;
    private String sifra;
    private LocalDate datumRodjenja;
    private List<Slike> slike;

    public ZupljaninBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ZupljaninBuilder ime(String ime) {
        this.ime = ime;
        return this;
    }

    public ZupljaninBuilder prezime(String prezime) {
        this.prezime = prezime;
        return this;
    }

    public ZupljaninBuilder sifra(String sifra) {
        this.sifra = sifra;
        return this;
    }

    public ZupljaninBuilder datumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
        return this;
    }

    public ZupljaninBuilder slike(List<Slike> slike) {
        this.slike = slike;
        return this;
    }

    public Zupljanin createZupljanin() {
        return new Zupljanin(id, ime, prezime, sifra, datumRodjenja);
    }
}