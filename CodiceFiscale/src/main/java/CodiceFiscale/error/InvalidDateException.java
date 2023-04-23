package CodiceFiscale.error;

public class InvalidDateException extends RuntimeException{
    public InvalidDateException() {this("Invalid format.");}

    public InvalidDateException(String message) {super(message);}
}
