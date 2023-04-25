package CodiceFiscale;

import CodiceFiscale.error.InvalidInputException;
import CodiceFiscale.fiscalcode.FiscalCode;
import CodiceFiscale.fiscalcode.FiscalCodeChecker;
import CodiceFiscale.fiscalcode.FiscalCodeGenerator;
import CodiceFiscale.person.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        handleXMLFiles();
        handleJSONFiles();
    }

    public static void handleXMLFiles() {
        String cityCodesFileNameXML = "./TestFiles/Comuni.xml";
        String fiscalCodesFileNameXML = "./TestFiles/CodiciFiscali.xml";
        String peopleFileNameXML = "./TestFiles/InputPersone.xml";
        try {
            
            Map<String, String> cityCodes = XML.getCityCodes(cityCodesFileNameXML);

            FiscalCodeChecker checker = new FiscalCodeChecker(cityCodes);
            FiscalCodeGenerator generator = new FiscalCodeGenerator(cityCodes);

            List<String> allFiscalCodes = XML.getFiscalCodes(fiscalCodesFileNameXML);
            ArrayList<FiscalCode> validFiscalCodes =
                    getValidCodes(XML.getFiscalCodes(fiscalCodesFileNameXML), checker);

            ArrayList<Person> people = XML.getPeople(peopleFileNameXML);

            List<Person> peopleWithFiscalCodeInFile =
                    getPeopleWithFiscalCodeInList(people,validFiscalCodes,generator);

            List<Person> personWithoutFiscalCodeInFile = new ArrayList<>(people);
            personWithoutFiscalCodeInFile.removeAll(peopleWithFiscalCodeInFile);


        } catch (Exception e) {
            System.out.println("Errore nella lettura dei file XML:");
            System.out.println(e.getMessage());
        }
    }

    public static void handleJSONFiles() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String cityCodesFileNameJson = "./TestFiles/Comuni.json";
        String fiscalCodesFileNameJson = "./TestFiles/CodiciFiscali.json";
        String peopleFileNameJson = "./TestFiles/InputPersone.json";
        String outputFileNameJson = "./Output/codiciPersone.json";

        try {
            Map<String, String> cityCodes = JSON.getCityCodes(cityCodesFileNameJson);

            FiscalCodeChecker checker = new FiscalCodeChecker(cityCodes);
            FiscalCodeGenerator generator = new FiscalCodeGenerator(cityCodes);

            List<String> allFiscalCodes = JSON.getFiscalCodes(fiscalCodesFileNameJson);
            List<FiscalCode> validFiscalCodes = getValidCodes(allFiscalCodes, checker);
            List<String> invalidFiscalCodes = new ArrayList<>(allFiscalCodes);
            invalidFiscalCodes.removeAll(
                    validFiscalCodes.stream().map(FiscalCode::getCode).toList()
            );


            List<Person> people =
                    JSON.getPeople(peopleFileNameJson);

            List<Person> peopleWithFiscalCodeInFile =
                    getPeopleWithFiscalCodeInList(people,validFiscalCodes,generator);

            List<String> unmatchedFiscalCodes = new ArrayList<>(allFiscalCodes);
            unmatchedFiscalCodes.removeAll(invalidFiscalCodes);
            unmatchedFiscalCodes.removeAll(
                    peopleWithFiscalCodeInFile.stream().map(p -> p.getFiscalCode().getCode()).toList()
            );

            JSON.writeOutput(people,invalidFiscalCodes, unmatchedFiscalCodes, outputFileNameJson);
        } catch (Exception e) {
            System.out.println("Errore nella lettura dei file json:");
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<FiscalCode> getValidCodes(List<String> allFiscalCodes,
                                                      FiscalCodeChecker checker) {
        ArrayList<FiscalCode> validFiscalCodes = new ArrayList<>();
        for (String code : allFiscalCodes) {
            try {
                validFiscalCodes.add(new FiscalCode(code, checker));
            } catch (InvalidInputException ignored) {}
        }
        return validFiscalCodes;
    }

    public static List<Person> getPeopleWithFiscalCodeInList
            (List<Person> allPeople, List<FiscalCode> fiscalCodes,FiscalCodeGenerator generator){


        List<String> fiscalCodeStrings = fiscalCodes.stream().map(FiscalCode::getCode).toList();
        ArrayList<Person> peopleWithFiscalCodeInList = new ArrayList<>();

        for(Person person : allPeople){
            person.setFiscalCode(generator);
            if(fiscalCodeStrings.contains(person.getFiscalCode().getCode())){
                peopleWithFiscalCodeInList.add(person);
            }else{
                person.resetFiscalCode();
            }
        }

        return peopleWithFiscalCodeInList;
    }
}
