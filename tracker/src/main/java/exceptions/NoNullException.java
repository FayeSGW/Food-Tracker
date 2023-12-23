package exceptions;

public class NoNullException extends Exception{
    public NoNullException() {}

    public NoNullException(String message) {
        super(message);
    }
}
