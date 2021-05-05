package exceptions;

public class InvalidTransactionIdException extends Exception {
    public InvalidTransactionIdException() { super(); }
    public InvalidTransactionIdException(String msg) { super(msg); }
}
