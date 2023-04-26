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
        cityCodes.put("ALCARA LI FUSI","A177");
        generator = new FiscalCodeGenerator(cityCodes);
    }

    @Test
    public void shouldSucceedToGenerateFiscalCode(){
        Person giuse = new Person("GIUSEPPE","MUSSO",
                Sex.M,"ALCARA LI FUSI", "1940-04-27");
        giuse.setFiscalCode(generator);
        Assertions.assertEquals("MSSGPP40D27A177B",giuse.getFiscalCode().getCode());
    }
}
