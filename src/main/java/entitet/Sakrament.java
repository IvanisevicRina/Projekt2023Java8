package entitet;

import java.io.Serializable;
import java.util.Set;

public final class Sakrament extends Entitet implements VanjskoOdrzavanje, Serializable {

    private String sifra;
    private String naziv;
    private Set<Zupljanin> zupljani;


    private LiturgijskoRazdoblje liturgijskoRazdoblje;


    public Sakrament(Long id, String sifra, String naziv, Set<Zupljanin> zupljani, LiturgijskoRazdoblje liturgijskoRazdoblje) {
        super(id);
        this.sifra = sifra;
        this.naziv = naziv;
        this.zupljani = zupljani;
        this.liturgijskoRazdoblje = liturgijskoRazdoblje;
    }

    public LiturgijskoRazdoblje getLiturgijskoRazdoblje() {
        return liturgijskoRazdoblje;
    }

    public void setLiturgijskoRazdoblje(LiturgijskoRazdoblje liturgijskoRazdoblje) {
        this.liturgijskoRazdoblje = liturgijskoRazdoblje;
    }

    @Override
    public String toString() {
        return "Sakrament{" +
                "sifra='" + sifra + '\'' +
                ", naziv='" + naziv + '\'' +
                ", zupljani=" + zupljani +
                ", liturgijskoRazdoblje=" + liturgijskoRazdoblje +
                '}';
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


    public Set<Zupljanin> getZupljani() {
        return zupljani;
    }

    public void setZupljani(Set<Zupljanin> zupljani) {
        this.zupljani = zupljani;
    }

    @Override
    public void lokacijaDvorista(String adresaDvorista) {

    }
}
