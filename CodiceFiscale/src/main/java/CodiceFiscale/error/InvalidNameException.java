package CodiceFiscale.error;

public class InvalidNameException extends RuntimeException{
    public InvalidNameException() {
        this("Name not valid.");
    }

    public InvalidNameException(String message) {
        super(message);
    }
}
