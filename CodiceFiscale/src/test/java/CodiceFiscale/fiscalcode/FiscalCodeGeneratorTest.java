package CodiceFiscale.fiscalcode;

import CodiceFiscale.person.Person;
import CodiceFiscale.person.Sex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class FiscalCodeGeneratorTest {

    static FiscalCodeGenerator generator;

    @BeforeAll
    public static void initializeMap(){
        Map<String, String> cityCodes = new HashMap<>(1);
        cityCodes.put("BRESCIA","B157");
        generator = new FiscalCodeGenerator(cityCodes);
    }

    @Test
    public void shouldSucceedToGenerateFiscalCode(){
        Person mario = new Person("Ivan","Abrami", Sex.M,"BRESCIA", "2003-12-25");
        mario.setFiscalCode(generator);
        Assertions.assertEquals("BRMVNI03T25B157K",mario.getFiscalCode().getCode());
    }
}
