package iznimke;

public class ZupljaninDuplikatException extends Exception {
    public ZupljaninDuplikatException(String message) {
        super("ZupljaniDuplikatException "+message);
    }

    public ZupljaninDuplikatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZupljaninDuplikatException(Throwable cause) {
        super(cause);
    }

    public ZupljaninDuplikatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}