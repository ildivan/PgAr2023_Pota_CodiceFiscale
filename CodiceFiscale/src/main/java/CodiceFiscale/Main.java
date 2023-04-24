package CodiceFiscale;

import CodiceFiscale.error.InvalidDateException;
import CodiceFiscale.error.InvalidFiscalCodeException;
import CodiceFiscale.fiscalcode.FiscalCode;
import CodiceFiscale.fiscalcode.FiscalCodeChecker;
import CodiceFiscale.fiscalcode.FiscalCodeGenerator;
import CodiceFiscale.person.Person;
import CodiceFiscale.person.Sex;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Main
{
    public static void main( String[] args )
    {

        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        String cityCodesFileName = "./TestFiles/Comuni.xml";

        HashMap<String,String> cityCodes = getCityCodes(xmlif,cityCodesFileName);
        FiscalCodeChecker checker = new FiscalCodeChecker(cityCodes);
        FiscalCodeGenerator generator = new FiscalCodeGenerator(cityCodes);

        String fiscalCodesFileName = "./TestFiles/CodiciFiscali.xml";
        ArrayList<FiscalCode> validFiscalCodes = getValidCodes(getFiscalCodes(xmlif,fiscalCodesFileName),checker);

        String peopleFileName = "./TestFiles/InputPersone.xml";
        ArrayList<Person> people = getPeople(xmlif,peopleFileName);
    }

    public static HashMap<String,String> getCityCodes(XMLInputFactory xmlif,String filename){
        HashMap<String,String> cityCodesMap = new HashMap<>();
        ArrayList<String> cityNames = new ArrayList<>();
        ArrayList<String> cityCodes = new ArrayList<>();

        XMLStreamReader xmlr;
        try {
            xmlr = xmlif.createXMLStreamReader(filename,
                    new FileInputStream(filename));

            String lastTag = "";
            while (xmlr.hasNext()) {
                switch (xmlr.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                    case XMLStreamConstants.CHARACTERS -> {
                        if(xmlr.getText().trim().length() > 0){
                            if (lastTag.equals("nome")) {
                                cityNames.add(xmlr.getText());
                            } else if (lastTag.equals("codice")) {
                                cityCodes.add(xmlr.getText());
                            }
                        }
                    }
                }
                xmlr.next();
            }

            for (int i = 0; i < cityNames.size(); i++) {
                cityCodesMap.put(cityNames.get(i),cityCodes.get(i));
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura dei comuni.:");
            System.out.println(e.getMessage());
        }
        return cityCodesMap;
    }

    public static ArrayList<String> getFiscalCodes(XMLInputFactory xmlif, String filename) {
        ArrayList<String> fiscalCodes = new ArrayList<>();

        XMLStreamReader xmlr;
        try {
            xmlr = xmlif.createXMLStreamReader(filename,
                    new FileInputStream(filename));

            String lastTag = "";
            while (xmlr.hasNext()) {
                switch (xmlr.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                    case XMLStreamConstants.CHARACTERS -> {
                        if(xmlr.getText().trim().length() > 0){
                            if (lastTag.equals("codice")) {
                                fiscalCodes.add(xmlr.getText());
                            }
                        }
                    }
                }
                xmlr.next();
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura dei codici fiscali:");
            System.out.println(e.getMessage());
        }
        return fiscalCodes;
    }

    public static ArrayList<FiscalCode> getValidCodes(ArrayList<String> allFiscalCodes,
                                                      FiscalCodeChecker checker) {
        ArrayList<FiscalCode> validFiscalCodes = new ArrayList<>();
        for (String code : allFiscalCodes) {
            try{
                validFiscalCodes.add(new FiscalCode(code,checker));
            }catch(InvalidFiscalCodeException ignored){}
        }
        return validFiscalCodes;
    }

    public static ArrayList<Person> getPeople(XMLInputFactory xmlif, String filename) {
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> surnames = new ArrayList<>();
        ArrayList<String> sexes = new ArrayList<>();
        ArrayList<String> citiesOfBirth = new ArrayList<>();
        ArrayList<String> datesOfBirth = new ArrayList<>();


        XMLStreamReader xmlr;
        try {
            xmlr = xmlif.createXMLStreamReader(filename,
                    new FileInputStream(filename));

            String lastTag = "";
            while (xmlr.hasNext()) {
                switch (xmlr.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                    case XMLStreamConstants.CHARACTERS -> {
                        if(xmlr.getText().trim().length() > 0){
                            switch (lastTag) {
                                case "nome" -> names.add(xmlr.getText());
                                case "cognome" -> surnames.add(xmlr.getText());
                                case "sesso" -> sexes.add(xmlr.getText());
                                case "comune_nascita" -> citiesOfBirth.add(xmlr.getText());
                                case "data_nascita" -> datesOfBirth.add(xmlr.getText());
                            }
                        }
                    }
                }
                xmlr.next();
            }

            for (int i = 0; i < names.size(); i++) {
                people.add(createPerson(
                        names.get(i),surnames.get(i),sexes.get(i),citiesOfBirth.get(i),datesOfBirth.get(i)
                ));
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura delle persone: ");
            System.out.println(e.getMessage());
        }
        return people;
    }

    public static Person createPerson(String name, String surname, String sexString,
                                      String cityOfBirth, String dateOfBirth){
        Sex sex = (sexString.equals("M") ? Sex.M : (sexString.equals("F") ? Sex.F : null));

        checkDateFormat(dateOfBirth);
        String[] dividedDate = dateOfBirth.split("-");

        int year = Integer.parseInt(dividedDate[0]);
        int month = Integer.parseInt(dividedDate[1]);
        int day = Integer.parseInt(dividedDate[2]);

        return new Person(name, surname, sex, cityOfBirth, year, month, day);
    }

    public static void checkDateFormat(String dateOfBirth) {
        boolean validFormat = dateOfBirth.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
        if (!validFormat) {
            throw new InvalidDateException(String.format("Date is not formatted correctly: %s", dateOfBirth));
        }
    }
}
