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

    /**
     * Parses a JSON string into an array of Stuff objects representing cast members.
     *
     * @param json The JSON string to be parsed.
     * @return An array of Stuff objects if parsing is successful, null otherwise.
     */
    public Stuff[] parseCast(String json){
        try {
            Gson gson = new Gson();
            // Deserialize the JSON string into an array of raw Map objects
            // Note: This causes a raw type warning; consider parameterizing the Map
            Map[] map = gson.fromJson(json, Map[].class);
            Stuff[] stuff = new Stuff[map.length];
            for (int i = 0; i < map.length; i++) {
                // Unchecked conversion from raw Map to parameterized Map<String, Object>
                Map<String, Object> entry = map[i];
                String name = (String) entry.get("name");
                String character = (String)entry.get("character");
                double d = (double)entry.get("id");
                int id = (int)d ;
                d = (double) entry.get("order");
                int order = (int)d;
                // Create a new Cast object and add it to the array
                stuff[i] = new Cast(name, id, character,order);
            }
            return stuff;
        } catch (Exception e){
            // Print the stack trace if an exception occurs during parsing
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses a JSON string into an array of Stuff objects representing crew members.
     *
     * @param json The JSON string to be parsed.
     * @return An array of Stuff objects if parsing is successful, null otherwise.
     */
    public Stuff[] parseCrew(String json){
        try {
            Gson gson = new Gson();
            // Deserialize the JSON string into an array of raw Map objects
            // Note: This causes a raw type warning; consider parameterizing the Map
            Map[] map = gson.fromJson(json, Map[].class);
            Stuff[] stuff = new Stuff[map.length];
            for (int i = 0; i < map.length; i++) {
                Map<String, Object> entry = map[i];
                String name = (String) entry.get("name");
                String job = (String)entry.get("job");
                double d = (double)entry.get("id");
                int id = (int)d ;
                stuff[i] = new Crew(name, id, job);
            }
            return stuff;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
*