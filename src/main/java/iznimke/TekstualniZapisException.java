package iznimke;
public class TekstualniZapisException extends Exception {
    public TekstualniZapisException(String message) {
        super("TekstualniZapisException: "+message);
    }
}
