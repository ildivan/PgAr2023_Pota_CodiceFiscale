package CodiceFiscale;

import CodiceFiscale.error.InvalidDateException;
import CodiceFiscale.fiscalcode.FiscalCodeChecker;
import CodiceFiscale.fiscalcode.FiscalCodeGenerator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {

        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        String cityCodesFileName = "./TestFiles/Comuni.xml";

        HashMap<String,String> cityCodes = getCityCodes(xmlif,cityCodesFileName);
        FiscalCodeChecker checker = new FiscalCodeChecker(cityCodes);
        FiscalCodeGenerator generator = new FiscalCodeGenerator(cityCodes);
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
        } catch (Exception e) {
            System.out.println("Errore nella lettura dei comuni.:");
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < cityNames.size(); i++) {
            cityCodesMap.put(cityNames.get(i),cityCodes.get(i));
        }
        return cityCodesMap;
    }

    private static void checkDateFormat(String dateOfBirth) {
        boolean validFormat = dateOfBirth.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
        if (!validFormat) {
            throw new InvalidDateException("Date is not formatted correctly.");
        }
    }
}
