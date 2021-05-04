package exceptions;

public class InvalidPaymentException extends Exception {
    public InvalidPaymentException() { super(); }
    public InvalidPaymentException(String msg) { super(msg); }
}
