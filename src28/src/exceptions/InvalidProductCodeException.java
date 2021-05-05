package exceptions;

public class InvalidProductCodeException extends Exception {
    public InvalidProductCodeException() { super(); }
    public InvalidProductCodeException(String msg) { super(msg); }
}
