package CodiceFiscale.fiscalcode;

import CodiceFiscale.error.InvalidInputException;

public class FiscalCode {
    private final String code;

    public FiscalCode(){
        code = "ASSENTE";
    }

    //Lets you create a fiscal code instance,
    // but a FiscalCodeChecker is needed to ensure the correctness of the code.
    public FiscalCode(String fiscalCode, FiscalCodeChecker checker){
        if(checker.isValid(fiscalCode)){
            code = fiscalCode;
            return;
        }
        throw new InvalidInputException("Codice fiscale non valido.");
    }

    //Protected constructor, intended to be used ONLY by a FiscalCodeGenerator.
    protected FiscalCode(String generatedFiscalCode){
        code = generatedFiscalCode;
    }

    public String getCode() {
        return code;
    }
}
