package exceptions;

public class NoNegativeException extends Exception {
    public NoNegativeException() {}

    public NoNegativeException(String message) {
        super(message);
    }
}