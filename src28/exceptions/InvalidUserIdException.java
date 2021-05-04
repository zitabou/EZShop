package exceptions;

public class InvalidUserIdException extends Exception {
    public InvalidUserIdException() { super(); }
    public InvalidUserIdException(String msg) { super(msg); }
}
