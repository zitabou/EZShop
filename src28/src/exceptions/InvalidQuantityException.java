package exceptions;

public class InvalidQuantityException extends Exception {
    public InvalidQuantityException() { super(); }
    public InvalidQuantityException(String msg) { super(msg); }
}
