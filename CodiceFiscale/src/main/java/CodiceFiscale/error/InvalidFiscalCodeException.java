package CodiceFiscale.error;

public class InvalidFiscalCodeException extends RuntimeException{
    public InvalidFiscalCodeException() {
        this("Invalid fiscal code");
    }

    public InvalidFiscalCodeException(String message) {
        super(message);
    }
}
