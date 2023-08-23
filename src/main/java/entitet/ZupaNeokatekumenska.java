package entitet;

import java.io.Serializable;
import java.util.List;

public class ZupaNeokatekumenska extends Zupa implements Neokatekumensko, Serializable {

    public ZupaNeokatekumenska(Long id, String naziv, List<Sakrament> sakramenti, List<Svecenik> svecenici, List<Zupljanin> zupljani, List<OsobniSakrament> osobniSakramenti) {
        super(id, naziv, sakramenti, svecenici, zupljani, osobniSakramenti);
    }

    @Override
    public Zupljanin odrediNajboljegZupljaninaUGodini(Integer godina) {
        return null;
    }


    @Override
    public Zupljanin odrediZupljaninaSaNajboljimPonasanjem() {
        return null;
    }
}
