package iznimke;

public class ZupljaninDuplikatException extends RuntimeException {
    public ZupljaninDuplikatException(String message) {

        super("ZupljaniDuplikatException "+message);
    }
}