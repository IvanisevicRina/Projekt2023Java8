package entitet;

import java.io.Serializable;
import java.util.Set;

public class Sakrament extends Entitet implements Serializable {

    private String sifra;
    private String naziv;
    private Set<Zupljanin> zupljani;

    private static final long serialVersionUID = 7259607473434795722L;




    public Sakrament(Long id, String sifra, String naziv, Set<Zupljanin> zupljani) {
        super(id);
        this.sifra = sifra;
        this.naziv = naziv;
        this.zupljani = zupljani;
    }


    @Override
    public String toString() {
        return "Sakrament{" +
                "sifra='" + sifra + '\'' +
                ", naziv='" + naziv + '\'' +
                ", zupljani=" + zupljani +
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


}
