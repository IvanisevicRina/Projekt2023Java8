package iznimke;

public class DuplikatSifreException extends Exception {
    public DuplikatSifreException(String message) {
        super("DuplikatSifreException "+message);
    }

    public DuplikatSifreException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplikatSifreException(Throwable cause) {
        super(cause);
    }

    public DuplikatSifreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}