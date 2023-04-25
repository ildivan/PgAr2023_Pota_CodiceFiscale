package CodiceFiscale;

import CodiceFiscale.person.Person;
import CodiceFiscale.person.Sex;

import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XML {
    private static final XMLInputFactory xmlif = XMLInputFactory.newInstance();
    private static final XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

    public static HashMap<String, String> getCityCodes(String filepath)
            throws FileNotFoundException, XMLStreamException {
        HashMap<String, String> cityCodesMap = new HashMap<>();
        ArrayList<String> cityNames = new ArrayList<>();
        ArrayList<String> cityCodes = new ArrayList<>();

        XMLStreamReader xmlr;
        xmlr = xmlif.createXMLStreamReader(filepath,
                new FileInputStream(filepath));

        String lastTag = "";
        while (xmlr.hasNext()) {
            switch (xmlr.getEventType()) {
                case XMLStreamConstants.START_ELEMENT -> lastTag = xmlr.getLocalName();
                case XMLStreamConstants.CHARACTERS -> {
                    if (xmlr.getText().trim().length() > 0) {
                        if (lastTag.equals("nome")) {
                            cityNames.add(xmlr.getText().replaceAll("[^'\\-\\sa-zA-Z]",""));
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

    public static ArrayList<String> getFiscalCodes(String filepath)
            throws FileNotFoundException, XMLStreamException {
        ArrayList<String> fiscalCodes = new ArrayList<>();

        XMLStreamReader xmlr;

        xmlr = xmlif.createXMLStreamReader(filepath,
                new FileInputStream(filepath));

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

    public static ArrayList<Person> getPeople(String filepath)
            throws FileNotFoundException, XMLStreamException {
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> surnames = new ArrayList<>();
        ArrayList<String> sexes = new ArrayList<>();
        ArrayList<String> citiesOfBirth = new ArrayList<>();
        ArrayList<String> datesOfBirth = new ArrayList<>();


        XMLStreamReader xmlr;
        xmlr = xmlif.createXMLStreamReader(filepath,
                new FileInputStream(filepath));

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

    public static void writeOutput(List<Person> people,List<String> invalid, List<String> unmatched, String filepath){
        XMLStreamWriter xmlw;
        try {
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(filepath), "utf-8");
            xmlw.writeStartDocument("utf-8", "1.0");
            xmlw.writeStartElement("output");

            writePeople(xmlw,people);
            writeCodes(xmlw,invalid,unmatched);

            xmlw.writeEndElement();
            xmlw.writeEndDocument();
            xmlw.flush();
            xmlw.close();
        } catch (Exception e) {
            System.out.println("Errore nella scrittura");
        }
    }

    private static void writePeople(XMLStreamWriter xmlw, List<Person> people) throws XMLStreamException {
        xmlw.writeStartElement("persone");
        xmlw.writeAttribute("numero",Integer.toString(people.size()));
        for (int i = 0; i < people.size(); i++) {
            writePerson(xmlw,people.get(i),i);
        }
        xmlw.writeEndElement();
    }

    private static void writePerson(XMLStreamWriter xmlw, Person person, int id) throws XMLStreamException {
        xmlw.writeStartElement("persona");
        xmlw.writeAttribute("id", Integer.toString(id));

            xmlw.writeStartElement("nome");
            xmlw.writeCharacters(person.getName());
            xmlw.writeEndElement();

            xmlw.writeStartElement("cognome");
            xmlw.writeCharacters(person.getSurname());
            xmlw.writeEndElement();

            xmlw.writeStartElement("sesso");
            xmlw.writeCharacters(person.getSex().toString());
            xmlw.writeEndElement();

            xmlw.writeStartElement("comune_nascita");
            xmlw.writeCharacters(person.getCityOfBirth());
            xmlw.writeEndElement();

            xmlw.writeStartElement("data_nascita");
            xmlw.writeCharacters(person.getDateOfBirth());
            xmlw.writeEndElement();

            xmlw.writeStartElement("codice_fiscale");
            xmlw.writeCharacters(person.getFiscalCode().getCode());
            xmlw.writeEndElement();

        xmlw.writeEndElement();
    }

    private static void writeCodes(XMLStreamWriter xmlw, List<String> invalid, List<String> unmatched)
            throws XMLStreamException {
        xmlw.writeStartElement("codici");
        writeFiscalCodeSeries(xmlw,invalid, "invalidi");
        writeFiscalCodeSeries(xmlw,unmatched, "spaiati");
        xmlw.writeEndElement();
    }

    private static void writeFiscalCodeSeries(XMLStreamWriter xmlw, List<String> series, String name)
            throws XMLStreamException {
        xmlw.writeStartElement(name);
        xmlw.writeAttribute("numero",Integer.toString(series.size()));
        for (String code : series){
            writeCode(xmlw,code);
        }
        xmlw.writeEndElement();
    }

    private static void writeCode(XMLStreamWriter xmlw, String code) throws XMLStreamException {
        xmlw.writeStartElement("codice");
        xmlw.writeCharacters(code);
        xmlw.writeEndElement();
    }
}
