package entitet;

public interface Krscansko {

    default OsobniSakrament[] filtrirajOsobneSakramentePoZupljaninu(OsobniSakrament[] osobniSakramenti, Zupljanin zupljanin){
        OsobniSakrament[] osobniSakramentiZupljanina = new OsobniSakrament[osobniSakramenti.length];
        int a=0;
        for(OsobniSakrament oSakrament : osobniSakramenti){
            if(oSakrament.getZupljanin() == zupljanin){
                osobniSakramentiZupljanina[a] = oSakrament;
                a++;
            }
        }
        OsobniSakrament[] osobniSakramentiKojeVracam = new OsobniSakrament[a];
        System.arraycopy(osobniSakramentiZupljanina, 0, osobniSakramentiKojeVracam, 0, a);
        return osobniSakramentiKojeVracam;

    }
}
