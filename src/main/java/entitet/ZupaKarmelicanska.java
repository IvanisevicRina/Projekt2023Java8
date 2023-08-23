package entitet;

import java.io.Serializable;
import java.util.List;

public class ZupaKarmelicanska extends Zupa implements Krscansko, Serializable {

    public ZupaKarmelicanska(Long id, String naziv, List<Sakrament> sakramenti, List<Svecenik> svecenici, List<Zupljanin> zupljani, List<OsobniSakrament> osobniSakramenti) {
        super(id, naziv, sakramenti, svecenici, zupljani, osobniSakramenti);
    }

    @Override
    public Zupljanin odrediNajboljegZupljaninaUGodini(Integer godina) {
        return null;
    }


    @Override
    public OsobniSakrament[] filtrirajOsobneSakramentePoZupljaninu(OsobniSakrament[] osobniSakramenti, Zupljanin zupljanin) {
        return Krscansko.super.filtrirajOsobneSakramentePoZupljaninu(osobniSakramenti, zupljanin);
    }
}
