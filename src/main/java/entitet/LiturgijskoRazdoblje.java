package entitet;

import java.io.Serializable;

public enum LiturgijskoRazdoblje  implements Serializable {

    DOSASCE(1), KORIZMA(2),OSTATAK(3) ;
    private int oznaka;

    LiturgijskoRazdoblje(Integer oznaka) {
        this.oznaka = oznaka;
    }

    public Integer getOznaka() {
        return oznaka;
    }

    public void setOznaka(Integer oznaka) {
        this.oznaka = oznaka;
    }
}
