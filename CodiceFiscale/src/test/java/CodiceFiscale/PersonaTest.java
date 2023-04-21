package CodiceFiscale;

import static org.junit.Assert.assertTrue;

import CodiceFiscale.error.IncorrectFormatException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Rappresenta una persona, racchiude i dati necessari per lavorare con il codice fiscale.
 */
public class PersonaTest {

    @Test
    public void shouldCreatePerson(){
        boolean succeded = true;
        try{
            Persona test = new Persona(0,"Mario","Rossi",
                    Sesso.M,"Milano","2003-12-25");
        }catch(IncorrectFormatException e){
            succeded = false;
        }

        Assert.assertTrue(succeded);
    }

    @Test
    public void shouldFailToCreatePersonWithWrongNumberOfDigitsInYear(){
        boolean failed;
        try{
            Persona test = new Persona(0,"Mario","Rossi",
                    Sesso.M,"Milano","20035-12-25");
            failed = false;
        }catch(IncorrectFormatException e){
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void shouldFailToCreatePersonWithWrongNumberOfDigitsInMonth(){
        boolean failed;
        try{
            Persona test = new Persona(1,"Mario","Rossi",
                    Sesso.M,"Milano","2003-122-25");
            failed = false;
        }catch(IncorrectFormatException e){
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void shouldFailToCreatePersonWithWrongNumberOfDigitsInDay(){
        boolean failed;
        try{
            Persona test = new Persona(2,"Mario","Rossi",
                    Sesso.M,"Milano","2003-12-2");
            failed = false;
        }catch(IncorrectFormatException e){
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void shouldFailToCreatePersonWithWrongDateSeparator(){
        boolean failed;
        try{
            Persona test = new Persona(3,"Mario","Rossi",
                    Sesso.M,"Milano","2003/12--25");
            failed = false;
        }catch(IncorrectFormatException e){
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidCharactersInDate(){
        boolean failed;
        try{
            Persona test = new Persona(4,"Mario","Rossi",
                    Sesso.M,"Milano","20d3-12- 2\n5");
            failed = false;
        }catch(IncorrectFormatException e){
            failed = true;
        }
        Assert.assertTrue(failed);
    }
}
