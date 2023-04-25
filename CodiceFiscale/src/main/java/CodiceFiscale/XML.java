package CodiceFiscale;

import CodiceFiscale.person.Person;
import CodiceFiscale.person.Sex;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class XML {
    private static final XMLInputFactory xmlif = XMLInputFactory.newInstance();

    public static HashMap<String, String> getCityCodes(String filename)
            throws FileNotFoundException, XMLStreamException {
        HashMap<String, String> cityCodesMap = new HashMap<>();
        ArrayList<String> cityNames = new ArrayList<>();
        ArrayList<String> cityCodes = new ArrayList<>();

        XMLStreamReader xmlr;
        xmlr = xmlif.createXMLStreamReader(filename,
                new FileInputStream(filename));

        String lastTag = "";
        while (xmlr.hasNext()) {
            switch (xmlr.getEventType()) {
                case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                case XMLStreamConstants.CHARACTERS -> {
                    if (xmlr.getText().trim().length() > 0) {
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
            cityCodesMap.put(cityNames.get(i), cityCodes.get(i));
        }

        return cityCodesMap;
    }

    public static ArrayList<String> getFiscalCodes(String filename)
            throws FileNotFoundException, XMLStreamException {
        ArrayList<String> fiscalCodes = new ArrayList<>();

        XMLStreamReader xmlr;

        xmlr = xmlif.createXMLStreamReader(filename,
                new FileInputStream(filename));

        String lastTag = "";
        while (xmlr.hasNext()) {
            switch (xmlr.getEventType()) {
                case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                case XMLStreamConstants.CHARACTERS -> {
                    if (xmlr.getText().trim().length() > 0) {
                        if (lastTag.equals("codice")) {
                            fiscalCodes.add(xmlr.getText());
                        }
                    }
                }
            }
            xmlr.next();
        }
        return fiscalCodes;
    }

    public static ArrayList<Person> getPeople(String filename)
            throws FileNotFoundException, XMLStreamException {
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> surnames = new ArrayList<>();
        ArrayList<String> sexes = new ArrayList<>();
        ArrayList<String> citiesOfBirth = new ArrayList<>();
        ArrayList<String> datesOfBirth = new ArrayList<>();


        XMLStreamReader xmlr;
        xmlr = xmlif.createXMLStreamReader(filename,
                new FileInputStream(filename));

        String lastTag = "";
        while (xmlr.hasNext()) {
            switch (xmlr.getEventType()) {
                case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                case XMLStreamConstants.CHARACTERS -> {
                    if (xmlr.getText().trim().length() > 0) {
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
            Sex sex = (sexes.get(i).equals("M") ? Sex.M : (sexes.get(i).equals("F") ? Sex.F : null));
            people.add(new Person(
                    names.get(i), surnames.get(i), sex, citiesOfBirth.get(i), datesOfBirth.get(i))
            );
        }
        return people;
    }

    

}
