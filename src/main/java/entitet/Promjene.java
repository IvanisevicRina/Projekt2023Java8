package entitet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Promjene<T, U> implements Serializable {
    private static final long serialVersionUID = -2691492564381033481L;
    private T staraVrijednost;
    private T novaVrijednost;
    private U objektPromjene;

    private String opis;
    private String rola;
    private String datumIVrijeme;

    public Promjene() {
    }



    public T getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(T staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public T getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(T novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public U getObjektPromjene() {
        return objektPromjene;
    }

    public void setObjektPromjene(U objektPromjene) {
        this.objektPromjene = objektPromjene;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }

    public String getDatumIVrijeme() {
        return datumIVrijeme;
    }

    public void setDatumIVrijeme(String datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promjene<?, ?> promjene = (Promjene<?, ?>) o;
        return Objects.equals(datumIVrijeme, promjene.datumIVrijeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datumIVrijeme);
    }
    public Promjene(T staraVrijednost, T novaVrijednost, U objektPromjene, String opis, String rola, String datumIVrijeme) {
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.objektPromjene = objektPromjene;
        this.opis = opis;
        this.rola = rola;
        this.datumIVrijeme = datumIVrijeme;
    }
}





