package CodiceFiscale.error;

public class IncorrectFormatException extends Exception{
    public IncorrectFormatException() {this("Formato non corretto.");}

    public IncorrectFormatException(String message) {super(message);}
}
