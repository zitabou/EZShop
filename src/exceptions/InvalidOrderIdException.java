package exceptions;

public class InvalidOrderIdException extends Exception {
    public InvalidOrderIdException() { super(); }
    public InvalidOrderIdException(String msg) { super(msg); }
}
