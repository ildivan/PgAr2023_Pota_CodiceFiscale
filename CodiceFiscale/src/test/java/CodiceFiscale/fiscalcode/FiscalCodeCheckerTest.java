package CodiceFiscale.fiscalcode;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class FiscalCodeCheckerTest {

    static FiscalCodeChecker fiscalChecker;

    @BeforeAll
    public static void initializeMap(){
        Map<String, String> cityCodes = new HashMap<>(1);
        cityCodes.put("notimportant","L584");
        fiscalChecker = new FiscalCodeChecker(cityCodes);
    }

    @Test
    public void shouldBeValid(){
        Assertions.assertTrue(fiscalChecker.isValid("RRAMHL24M31L584H"));
    }

    @Test
    public void shouldBeTooLong(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24M31L584HG"));
    }

    @Test
    public void shouldBeTooShort(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24M31L584"));
    }

    @Test
    public void shouldFailInvalidName(){
        Assertions.assertFalse(fiscalChecker.isValid("ERAMHL24M31L584H"));
    }

    @Test
    public void shouldFailInvalidSurname(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMIL24M31L584H"));
    }

    @Test
    public void shouldFailInvalidPositionOfXInName(){
        Assertions.assertFalse(fiscalChecker.isValid("XRAMHL24M31L584H"));
    }

    @Test
    public void shouldFailInvalidPositionOfXInSurname(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMXL24M31L584H"));
    }

    @Test
    public void shouldFailDayBetween32And40(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24M32L584H"));
    }

    @Test
    public void shouldFailDayBelow1(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24M00L584H"));
    }

    @Test
    public void shouldFailDayOver71(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24M72L584H"));
    }

    @Test
    public void shouldFailDay29OfFebruary(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24B29L584H"));
    }

    @Test
    public void shouldFailDay29OfFebruaryFemale(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24B69L584H"));
    }

    @Test
    public void shouldFailDay31OfApril(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24D31L584H"));
    }

    @Test
    public void shouldFailDay31OfAprilFemale(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24D71L584H"));
    }

    @Test
    public void shouldFailInvalidControlChar(){
        Assertions.assertFalse(fiscalChecker.isValid("RRAMHL24D71L584Z"));
    }

}
