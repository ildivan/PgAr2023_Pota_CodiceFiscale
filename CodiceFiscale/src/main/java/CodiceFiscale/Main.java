package CodiceFiscale;

import CodiceFiscale.error.InvalidInputException;
import CodiceFiscale.fiscalcode.FiscalCode;
import CodiceFiscale.fiscalcode.FiscalCodeChecker;
import CodiceFiscale.fiscalcode.FiscalCodeGenerator;
import CodiceFiscale.person.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        handleXMLFiles();
        handleJSONFiles();
    }

    public static void handleXMLFiles() {
        String cityCodesFilepathXML = "./TestFiles/Comuni.xml";
        String fiscalCodesFilepathXML = "./TestFiles/CodiciFiscali.xml";
        String peopleFilepathXML = "./TestFiles/InputPersone.xml";
        String outputFilepathXML = "./Output/codiciPersone.xml";

        try {
            System.out.println("Leggendo lista comuni da XML");
            Map<String, String> cityCodes = XML.getCityCodes(cityCodesFilepathXML);
            FiscalCodeChecker checker = new FiscalCodeChecker(cityCodes);
            FiscalCodeGenerator generator = new FiscalCodeGenerator(cityCodes);

            System.out.println("Leggendo codici fiscali da XML");
            List<String> allFiscalCodes = XML.getFiscalCodes(fiscalCodesFilepathXML);
            List<FiscalCode> validFiscalCodes = getValidCodes(allFiscalCodes, checker);
            List<String> invalidFiscalCodes = getInvalidCodes(allFiscalCodes,validFiscalCodes);

            System.out.println("Leggendo lista  persone da XML");
            List<Person> people = XML.getPeople(peopleFilepathXML);

            List<String> unmatchedFiscalCodes = getUnmatchedFiscalCodes(people,validFiscalCodes,generator);

            System.out.println("Writing to XML file.");
            XML.writeOutput(people,invalidFiscalCodes, unmatchedFiscalCodes, outputFilepathXML);
        } catch (Exception e) {
            System.out.println("Errore nella lettura dei file XML:");
            System.out.println(e.getMessage());
        }
    }

    public static void handleJSONFiles() {
        String cityCodesFilepathJson = "./TestFiles/Comuni.json";
        String fiscalCodesFilepathJson = "./TestFiles/CodiciFiscali.json";
        String peopleFilepathJson = "./TestFiles/InputPersone.json";
        String outputFilepathJson = "./Output/codiciPersone.json";

        try {
            System.out.println("Leggendo lista comuni da Json");
            Map<String, String> cityCodes = JSON.getCityCodes(cityCodesFilepathJson);
            FiscalCodeChecker checker = new FiscalCodeChecker(cityCodes);
            FiscalCodeGenerator generator = new FiscalCodeGenerator(cityCodes);

            System.out.println("Leggendo codici fiscali da Json");
            List<String> allFiscalCodes = JSON.getFiscalCodes(fiscalCodesFilepathJson);
            List<FiscalCode> validFiscalCodes = getValidCodes(allFiscalCodes, checker);
            List<String> invalidFiscalCodes = getInvalidCodes(allFiscalCodes,validFiscalCodes);

            System.out.println("Leggendo lista  persone da Json");
            List<Person> people = JSON.getPeople(peopleFilepathJson);

            List<String> unmatchedFiscalCodes = getUnmatchedFiscalCodes(people,validFiscalCodes,generator);

            System.out.println("Writing to Json file.");
            JSON.writeOutput(people,invalidFiscalCodes, unmatchedFiscalCodes, outputFilepathJson);
        } catch (Exception e) {
            System.out.println("Errore nella lettura dei file json:");
            System.out.println(e.getMessage());
        }
    }

    //Returns all the FiscalCode objects that are valid, according to the given FiscalCodeChecker.
    private static ArrayList<FiscalCode> getValidCodes(List<String> allFiscalCodes,
                                                      FiscalCodeChecker checker) {
        ArrayList<FiscalCode> validFiscalCodes = new ArrayList<>();
        for (String code : allFiscalCodes) {
            try {
                validFiscalCodes.add(new FiscalCode(code, checker));
            } catch (InvalidInputException ignored) {}
        }
        return validFiscalCodes;
    }

    private static List<String> getInvalidCodes(List<String> allFiscalCodes, List<FiscalCode> validFiscalCodes) {

        List<String> validFiscalCodeStrings = toListOfStrings(validFiscalCodes);

        List<String> invalidFiscalCodes =
                //Creates a stream of all fiscal codes
                allFiscalCodes.stream()
                        //Take only the fiscal codes that are not in validFiscalCodeStrings
                        .filter(code -> !validFiscalCodeStrings.contains(code))
                        //Convert to list
                        .toList();

        return invalidFiscalCodes;
    }



    //Returns a fiscal code's string list of the people who are not matched by a fiscal code in the file.
    private static List<String> getUnmatchedFiscalCodes
            (List<Person> people, List<FiscalCode> validFiscalCodes, FiscalCodeGenerator generator) {
        setPeopleWithFiscalCodeInList(people,validFiscalCodes,generator);

        List<String> matchedFiscalCodes = getMatchedFiscalCodes(people);

        List<String> unmatchedFiscalCodes =
                //Creates a stream of all valid fiscal codes
                toListOfStrings(validFiscalCodes).stream()
                        //Take only fiscal codes that are not in matchedFiscalCodes
                        .filter(code -> !matchedFiscalCodes.contains(code))
                        //Converts to list
                        .toList();

        return unmatchedFiscalCodes;
    }

    //Returns a fiscal code's string list of the people who were matched in the file.
    private static List<String> getMatchedFiscalCodes(List<Person> people) {
        List<String> matchedFiscalCodes =
                people.stream()
                        //Creates a stream of the people's fiscal codes
                        .map(Person::getFiscalCode)
                        //Take only fiscal codes that aren't FiscalCode.ABSENT
                        .filter(code -> code != FiscalCode.ABSENT)
                        //Creates a stream of the matched fiscal codes strings
                        .map(FiscalCode::getCode)
                        //Converts to list
                        .toList();

        return matchedFiscalCodes;
    }

    //Generates the FiscalCode of every person, then resets to default the fiscal code
    //of the people who are not matched by any elements of fiscalCodes.
    public static void setPeopleWithFiscalCodeInList
            (List<Person> allPeople, List<FiscalCode> fiscalCodes,FiscalCodeGenerator generator){
        List<String> fiscalCodeStrings = toListOfStrings(fiscalCodes);

        for(Person person : allPeople){
            person.setFiscalCode(generator); //Generates fiscal code.
            if(!fiscalCodeStrings.contains(person.getFiscalCode().getCode())){
                person.resetFiscalCode();
            }
        }
    }

    //Converts a FiscalCode list to a String list
    private static List<String> toListOfStrings(List<FiscalCode> listOfCodes) {
        return listOfCodes.stream()
                .map(FiscalCode::getCode)
                .toList();
    }
}
