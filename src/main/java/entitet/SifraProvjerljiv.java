package entitet;

import iznimke.DuplikatSifreException;

public interface SifraProvjerljiv {
    void provjeraSifre(String sifra) throws DuplikatSifreException;
}