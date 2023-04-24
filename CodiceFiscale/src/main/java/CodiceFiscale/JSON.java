package CodiceFiscale;

import CodiceFiscale.person.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSON {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
}
