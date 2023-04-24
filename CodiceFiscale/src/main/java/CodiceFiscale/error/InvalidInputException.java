package CodiceFiscale.error;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException() {
        this("Name not valid.");
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
