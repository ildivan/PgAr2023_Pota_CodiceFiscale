package CodiceFiscale.fiscalcode;

import CodiceFiscale.person.Person;

import java.util.Map;

public class FiscalCodeGenerator {
    private final Map<String, String> cityCodes;

    public FiscalCodeGenerator(Map<String, String> cityCodes) {
        this.cityCodes = cityCodes;
    }


    public FiscalCode generateFiscalCode(Person person){
        return new FiscalCode("");
    }
}
