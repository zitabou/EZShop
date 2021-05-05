package exceptions;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() { super(); }
    public UnauthorizedException(String msg) { super(msg); }
}
