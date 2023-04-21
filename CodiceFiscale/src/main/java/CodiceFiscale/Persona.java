package CodiceFiscale;

import CodiceFiscale.error.IncorrectFormatException;

public class Persona {
    private final int id;
    private final String nome;
    private final String cognome;
    private final Sesso sesso;
    private final String comuneNascita;
    private final String annoNascita;
    private final String meseNascita;
    private final String giornoNascita;

    /**
     * @param id  Identificativo della persona.
     * @param nome Nome della persona.
     * @param cognome Cognome della persona.
     * @param sesso Può essere maschio (Sesso.M) o femmina (Sesso.F)
     * @param comuneNascita Stringa contenente il comune di nascita della persona.
     * @param dataNascita (Formattata nel seguente modo "YYYY-MM-DD")
     */
    public Persona(int id,String nome, String cognome, Sesso sesso,
                   String comuneNascita, String dataNascita) throws IncorrectFormatException{
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.comuneNascita = comuneNascita;
        boolean isValidFormat = dataNascita.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
        String[] data;
        if (isValidFormat) {
            data = dataNascita.split("-");
            annoNascita = data[0];
            meseNascita = data[1];
            giornoNascita = data[2];
        }else{
            throw new IncorrectFormatException("La data inserita non è corretta.");
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Sesso getSesso() {
        return sesso;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public String getAnnoNascita() {
        return annoNascita;
    }

    public String getMeseNascita() {
        return meseNascita;
    }

    public String getGiornoNascita() {
        return giornoNascita;
    }


}
