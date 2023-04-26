package CodiceFiscale;

import CodiceFiscale.person.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSON {
    //Instance of gson used to read from json files and to write to json files
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting().registerTypeAdapter(Person.class, getPersonSerializer()).create();

    //Custom serializer for Person class, required serializing fiscal code as a string and not as an object
    private static JsonSerializer<Person> getPersonSerializer() {
        return (src, typeOfSrc, context) -> {
            JsonObject jsonPerson = new JsonObject();

            jsonPerson.addProperty("nome", src.getName());
            jsonPerson.addProperty("cognome", src.getSurname());
            jsonPerson.addProperty("sesso", src.getSex().toString());
            jsonPerson.addProperty("comune_nascita", src.getCityOfBirth());
            jsonPerson.addProperty("data_nascita", src.getDateOfBirth());
            jsonPerson.addProperty("codice_fiscale", src.getFiscalCode().getCode());

            return jsonPerson;
        };
    }

    //Class used to serialize the data in the correct structure
    private static class Output {
        private List<Person> persone;
        private Codes codici;

        public Output(List<Person> persone, List<String> invalidi, List<String> spaiati) {
            this.persone = persone;
            this.codici = new Codes(invalidi, spaiati);
        }
    }

    //Class used to represent the field 'codici' in the json
    private static class Codes {
        private List<String> invalidi;
        private List<String> spaiati;

        public Codes(List<String> invalidi, List<String> spaiati) {
            this.invalidi = invalidi;
            this.spaiati = spaiati;
        }
    }

    //Reads city names and codes from the appropriate file and returns them as a map
    public static Map<String, String> getCityCodes(String filepath) throws IOException {
        FileReader fileReader = new FileReader(filepath);
        TypeToken<List<Map<String, String>>> mapType = new TypeToken<>() {};

        //Reads from json as a list of maps.
        List<Map<String,String>> foundCities = gson.fromJson(fileReader, mapType);

        //Puts all the maps together as a single map.
        Map<String, String> cityCodes = new HashMap<>();
        for ( Map<String,String> city : foundCities) {
            cityCodes.put(
                    //The current city name without any non-allowed characters.
                    city.get("nome").replaceAll("[^'\\-\\sa-zA-Z]",""),
                    //The current city code.
                    city.get("codice")
            );
        }

        return cityCodes;
    }

    //Reads fiscal codes from the appropriate json file and returns them as a list.
    public static List<String> getFiscalCodes(String filepath) throws IOException {
        FileReader fileReader = new FileReader(filepath);
        return Arrays.asList(gson.fromJson(fileReader,String[].class));
    }

    //Reads people's data from the appropriate json file and returns it as a list of Person instances.
    public static List<Person> getPeople(String filepath) throws IOException {
        FileReader fileReader = new FileReader(filepath);
        return Arrays.asList(gson.fromJson(fileReader,Person[].class));
    }

    //Write to json file the processed data:
    // people with their fiscal codes if not absent,
    // invalid fiscal codes
    // and unmatched fiscal codes.
    public static void writeOutput(List<Person> people,List<String> invalid, List<String> unmatched, String filepath)
            throws IOException {
        Output output = new Output(people,invalid,unmatched);
        FileWriter fw = new FileWriter(filepath);
        gson.toJson(output, fw);
        fw.close();
    }
}
