package CodiceFiscale.fiscalcode;

import CodiceFiscale.error.InvalidFiscalCodeException;
import CodiceFiscale.person.Person;

public class FiscalCode {
    private final String code;

    public FiscalCode(String fiscalCode, FiscalCodeChecker checker){
        if(checker.isValid(fiscalCode)){
            code = fiscalCode;
        }
        throw new InvalidFiscalCodeException();
    }

    protected FiscalCode(String generatedFiscalCode){
        code = generatedFiscalCode;
    }

    public String getCode() {
        return code;
    }
}
