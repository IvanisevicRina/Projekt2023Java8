package entitet;

import java.io.Serializable;

public enum Sakramenti implements Serializable {

    KRSTENJE(1,"krštenje"), PRICEST(2,"pričest"),KRIZMA(3,"krizma"), ZENIDBA(4,"zenidba"), BOLESNICKO_POMAZANJE(5,"bolesničko pomazanje"), SVETI_RED(6, "sveti red");
    private int oznaka;
    private String naziv;

    Sakramenti(int oznaka, String naziv) {
        this.oznaka = oznaka;
        this.naziv = naziv;
    }

    public int getOznaka() {
        return oznaka;
    }

    public String getNaziv() {
        return naziv;
    }
}
