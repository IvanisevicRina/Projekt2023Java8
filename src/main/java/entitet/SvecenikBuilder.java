package entitet;

import java.io.Serializable;
import java.time.LocalDate;

public class SvecenikBuilder  implements Serializable {
    private Long id;

    private String ime;
    private String prezime;
    private String sifra;
    private String titula;
    private LocalDate datumRodjenja;



    public SvecenikBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public SvecenikBuilder setIme(String ime) {
        this.ime = ime;
        return this;
    }

    public SvecenikBuilder setPrezime(String prezime) {
        this.prezime = prezime;
        return this;
    }

    public SvecenikBuilder setSifra(String sifra) {
        this.sifra = sifra;
        return this;
    }

    public SvecenikBuilder setTitula(String titula) {
        this.titula = titula;
        return this;
    }

    public SvecenikBuilder setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
        return this;
    }

    public Svecenik createSvecenik(){return new Svecenik(id, ime, prezime,sifra, titula, datumRodjenja);}
}
