package entitet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class OsobniSakrament extends Entitet  implements Serializable {

    private Sakrament sakrament;

    private Zupljanin zupljanin;

    private LocalDateTime datumIVrijeme;

    private Crkva crkva;



    public OsobniSakrament(Long id, Sakrament sakrament, Zupljanin zupljanin, LocalDateTime datumIVrijeme, Crkva crkva) {
        super(id);
        this.sakrament = sakrament;
        this.zupljanin = zupljanin;
        this.datumIVrijeme = datumIVrijeme;
        this.crkva = crkva;
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

}
