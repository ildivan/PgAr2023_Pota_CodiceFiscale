package CodiceFiscale;

import CodiceFiscale.person.Person;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSON {
    private static final Gson gson = new Gson();

    private static class Output {
        private List<Person> persone;
        private Codici codici;

        public Output(List<Person> persone, List<String> invalidi, List<String> spaiati) {
            this.persone = persone;
            this.codici = new  Codici(invalidi, spaiati);
        }
    }

    private static class Codici {
        private List<String> invalidi;
        private List<String> spaiati;

        public Codici(List<String> invalidi, List<String> spaiati) {
            this.invalidi = invalidi;
            this.spaiati = spaiati;
        }
    }

    public static Map<String, String> getCityCodes(String filename) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filename));
        TypeToken<List<Map<String, String>>> mapType = new TypeToken<>() {
        };
        //Reads from json as a list of maps and puts all the maps together as a single map.
        Map<String, String> cityCodes =
                gson.fromJson(reader, mapType)
                        .stream()
                        .collect(Collectors.toMap(s -> s.get("nome"), s -> s.get("codice")));

        return cityCodes;
    }

    public static List<String> getFiscalCodes(String filename) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filename));
        TypeToken<List<String>> listType = new TypeToken<>(){};

        return gson.fromJson(reader,listType);
    }

    public static List<Person> getPeople(String filename) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filename));
        return Arrays.asList(gson.fromJson(reader,Person[].class));
    }

    public static void writeOutput(List<Person> people,List<String> invalid, List<String> unmatched, String filename)
            throws IOException {
        GsonBuilder customGson = new GsonBuilder().setPrettyPrinting();
        customGson.registerTypeAdapter(Person.class, getPersonSerializer());

        Gson gson = customGson.create();

        Output output = new Output(people,invalid,unmatched);
        FileOutputStream fos = new FileOutputStream(filename);
        OutputStreamWriter ow = new OutputStreamWriter(fos);
        ow.write(gson.toJson(output));
        ow.flush();
    }

    private static JsonSerializer<Person> getPersonSerializer() {
        return new JsonSerializer<>() {
            @Override
            public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject jsonPerson = new JsonObject();

                jsonPerson.addProperty("nome", src.getName());
                jsonPerson.addProperty("cognome", src.getSurname());
                jsonPerson.addProperty("sesso", src.getSex().toString());
                jsonPerson.addProperty("comune_nascita", src.getCityOfBirth());
                jsonPerson.addProperty("data_nascita", src.getDateOfBirth());
                jsonPerson.addProperty("codice_fiscale", src.getFiscalCode().getCode());

                return jsonPerson;
            }
        };
    }
}
