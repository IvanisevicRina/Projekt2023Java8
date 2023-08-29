package entitet;
import java.util.HashMap;
import java.util.Map;
public class SveciKalendar {
    private Map<String, String> sveciImendani;

    public SveciKalendar() {
        sveciImendani = new HashMap<>();
        sveciImendani.put("29.08", "Elija");
        sveciImendani.put("30.08", "Tvrtko");
        sveciImendani.put("31.08", "Marin");
        sveciImendani.put("01.09", "Elija");
        sveciImendani.put("02.09", "Tvrtko");
        sveciImendani.put("03.09", "Marin");
        sveciImendani.put("04.09", "Mario");
        sveciImendani.put("05.09", "Roko");
        sveciImendani.put("06.09", "Rina");
        sveciImendani.put("07.09", "Gabrijela");
        sveciImendani.put("08.09", "Marija");
        sveciImendani.put("09.09", "Frane");
        sveciImendani.put("10.09", "Goran");
        sveciImendani.put("11.09", "Zvonimir");
        sveciImendani.put("12.09", "Marija");
        sveciImendani.put("13.09", "Klara");
        sveciImendani.put("14.09", "Lara");
        sveciImendani.put("15.09", "Petra");
        sveciImendani.put("16.09", "Matea");
        sveciImendani.put("17.09", "Šimun");
        sveciImendani.put("18.09", "Karlo");
        sveciImendani.put("19.09", "Leon");
        sveciImendani.put("20.09", "Ivana");
        sveciImendani.put("21.09", "Matija");
        sveciImendani.put("22.09", "Mauricije");
        sveciImendani.put("23.09", "Pavao");
        sveciImendani.put("24.09", "Mihovil");
        sveciImendani.put("25.09", "Zlata");
        sveciImendani.put("26.09", "Lana");
        sveciImendani.put("27.09", "Vinko");
        sveciImendani.put("28.09", "Ljudevit");
        sveciImendani.put("29.09", "Mihovil");
        sveciImendani.put("30.09", "Jerko");

    }
    public String getImeSveca(String datum) {
        return sveciImendani.get(datum);
    }


}
