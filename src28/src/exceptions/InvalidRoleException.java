package exceptions;

public class InvalidRoleException extends Exception {
    public InvalidRoleException() { super(); }
    public InvalidRoleException(String msg) { super(msg); }
}
