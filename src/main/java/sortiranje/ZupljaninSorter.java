package sortiranje;

import entitet.Zupljanin;

import java.util.Comparator;

public class ZupljaninSorter implements Comparator<Zupljanin> {
    @Override
    public int compare(Zupljanin z1, Zupljanin z2) {
        int r = z1.getPrezime().compareTo(z2.getPrezime()); // ako je r 0 onda su jednaki
        if (r != 0) {
            return r;
        }
        r = z1.getIme().compareTo(z2.getIme());
        if (r != 0) {
            return r;
        }
        return 0;
    }
}
