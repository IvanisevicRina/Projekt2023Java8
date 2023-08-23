package entitet;

import java.io.Serializable;

public record Crkva(String nazivCrkve)  implements Serializable {


    public Crkva(String nazivCrkve){
        this.nazivCrkve=nazivCrkve;
    }

    @Override
    public String nazivCrkve() {
        return nazivCrkve;
    }


}
