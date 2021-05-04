package exceptions;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException() { super(); }
    public InvalidUsernameException(String msg) { super(msg); }
}
