/**
 * ParseJson is a utility class designed to parse JSON strings into Java object arrays.
 * It uses the Gson library to perform JSON deserialization.
 */
import com.google.gson.Gson;

import java.util.Map;

public class ParseJson {
    /**
     * Constructs a new ParseJson object.
     * This constructor currently has no specific initialization logic.
     */
    public ParseJson(){}

    /**
     * Parses a JSON string into an array of Genre objects.
     *
     * @param json The JSON string to be parsed.
     * @return An array of Genre objects if parsing is successful, null otherwise.
     */
    public Genre[] parseGenre(String json){
        Gson gson = new Gson();
        // Deserialize the JSON string into an array of Genre objects
        Genre[] list = gson.fromJson(json, Genre[].class);
        return list;
    }
