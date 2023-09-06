package entitet;

import java.io.Serializable;
import java.time.LocalDateTime;

public non-sealed class OsobniSakrament extends Entitet  implements Serializable, VanjskoOdrzavanje {

    private Sakrament sakrament;

    private Zupljanin zupljanin;

    private LocalDateTime datumIVrijeme;

    private LiturgijskoRazdoblje liturgijskoRazdoblje;

    private Crkva crkva;



    public OsobniSakrament(Long id, Sakrament sakrament, Zupljanin zupljanin, LocalDateTime datumIVrijeme, Crkva crkva, LiturgijskoRazdoblje liturgija) {
        super(id);
        this.sakrament = sakrament;
        this.zupljanin = zupljanin;
        this.datumIVrijeme = datumIVrijeme;
        this.crkva = crkva;
        this.liturgijskoRazdoblje=liturgija;
    }

    @Override
    public String toString() {
        return "OsobniSakrament{" +
                "sakrament=" + sakrament +
                ", zupljanin=" + zupljanin +
                ", datumIVrijeme=" + datumIVrijeme +
                ", crkva=" + crkva +
                '}';
    }

    public Sakrament getSakrament() {
        return sakrament;
    }

    public void setSakrament(Sakrament sakrament) {
        this.sakrament = sakrament;
    }

    public Zupljanin getZupljanin() {
        return zupljanin;
    }

    public void setZupljanin(Zupljanin zupljanin) {
        this.zupljanin = zupljanin;
    }

    public LocalDateTime getDatumIVrijeme() {
        return datumIVrijeme;
    }

    public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }

    public Crkva getCrkva() {
        return crkva;
    }

    public void setCrkva(Crkva crkva) {
        this.crkva = crkva;
    }

    public LiturgijskoRazdoblje getLiturgijskoRazdoblje() {
        return liturgijskoRazdoblje;
    }

    public void setLiturgijskoRazdoblje(LiturgijskoRazdoblje liturgijskoRazdoblje) {
        this.liturgijskoRazdoblje = liturgijskoRazdoblje;
    }


    @Override
    public void lokacijaDvorista(String adresaDvorista) {
        System.out.println("ovo je adresa VANJSKOG ODRŽAVANJA:" + adresaDvorista);
    }
}
