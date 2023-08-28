package iznimke;
public class TekstualniZapisException extends Exception {
    public TekstualniZapisException(String message) {
        super("TekstualniZapisException: "+message);
    }

    public TekstualniZapisException(String message, Throwable cause) {
        super(message, cause);
    }

    public TekstualniZapisException(Throwable cause) {
        super(cause);
    }

    public TekstualniZapisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
