package entitet;

import java.io.Serializable;
import java.util.List;

public abstract class Zupa extends Entitet implements Serializable {

    private String naziv;
    private List<Sakrament> sakramenti;

    private List<Svecenik> svecenici;

    private  List<Zupljanin> zupljani;

    private List<OsobniSakrament> osobniSakramenti;

    public Zupa(Long id, String naziv, List<Sakrament> sakramenti, List<Svecenik> svecenici, List<Zupljanin> zupljani, List<OsobniSakrament> osobniSakramenti) {
        super(id);
        this.naziv = naziv;
        this.sakramenti = sakramenti;
        this.svecenici = svecenici;
        this.zupljani = zupljani;
        this.osobniSakramenti = osobniSakramenti;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<Sakrament> getSakramenti() {
        return sakramenti;
    }

    public void setSakramenti(List<Sakrament> sakramenti) {
        this.sakramenti = sakramenti;
    }

    public List<Svecenik> getSvecenici() {
        return svecenici;
    }

    public void setSvecenici(List<Svecenik> svecenici) {
        this.svecenici = svecenici;
    }

    public List<Zupljanin> getZupljani() {
        return zupljani;
    }

    public void setZupljani(List<Zupljanin> zupljani) {
        this.zupljani = zupljani;
    }

    public List<OsobniSakrament> getOsobniSakramenti() {
        return osobniSakramenti;
    }

    public void setOsobniSakramenti(List<OsobniSakrament> osobniSakramenti) {
        this.osobniSakramenti = osobniSakramenti;
    }

    public abstract Zupljanin odrediNajboljegZupljaninaUGodini(Integer godina);





}
