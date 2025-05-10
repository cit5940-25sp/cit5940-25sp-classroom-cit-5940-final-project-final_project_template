import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CSVReader is a class that implements the Reader interface.
 * It is designed to read CSV files, parse their contents, and load movie-related data.
 * It also uses the ParseJson class to handle JSON data within the CSV files.
 */
public class CSVReader implements Reader{
    // Instance of ParseJson used to parse JSON strings into Java objects
    private ParseJson parseJson;
    // TreeSet to store Movie objects, sorted according to their natural ordering
    private TreeSet<Movie> movies;
    private TreeSet<Stuff> stuffs;
    /**
     * Constructs a new CSVReader object.
     * Initializes the movies TreeSet and creates a new ParseJson instance.
     */
    public CSVReader(){
        // Initialize the TreeSet to store Movie objects
        movies = new TreeSet<>();
        stuffs = new TreeSet<>();
        // Create a new instance of ParseJson for JSON parsing
        parseJson = new ParseJson();
        loadDate();
    }

    /**
     * Loads movie data from two CSV files: tmdb_5000_movies.csv and tmdb_5000_credits.csv.
     */
    public void loadDate(){
        // Load movie information from the main CSV file
        loadFile("data/tmdb_5000_movies.csv");
        // Load extra information such as cast and crew from the additional CSV file
        loadExtraFile("data/tmdb_5000_credits.csv");
    }

    public TreeSet<Stuff> readStuffs(){
        return stuffs;
    }

    /**
     * Parses a CSV file and returns its contents as a list of string arrays.
     * Each inner array represents a row in the CSV file, with each element being a field.
     *
     * @param filePath The path to the CSV file to be parsed.
     * @return A list of string arrays containing the parsed CSV data.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private List<String[]> parseCsv(String filePath) throws IOException {
        // List to store the parsed CSV data
        List<String[]> data = new ArrayList<>();
        // Use try-with-resources to ensure the BufferedReader is closed automatically
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // StringBuilder to build the current line, especially useful for multi-line fields
            StringBuilder currentLine = new StringBuilder();
            // Flag to indicate whether the parser is currently inside a quoted field
            boolean inQuotes = false;
            // Variable to hold each line read from the file
            String line;
            // Read lines from the file until the end is reached
            while ((line = reader.readLine()) != null) {
                // Append the read line to the current line
                currentLine.append(line);
                // Check if the line ends with an open quote
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        inQuotes = !inQuotes;
                    }
                }
                // If not inside quotes, the line is complete and can be parsed
                if (!inQuotes) {
                    // List to store the fields of the current line
                    List<String> fields = new ArrayList<>();
                    // StringBuilder to build the current field
                    StringBuilder currentField = new StringBuilder();
                    // Reset the inQuotes flag for parsing the current line
                    inQuotes = false;
                    // Iterate through each character in the current line
                    for (int i = 0; i < currentLine.length(); i++) {
                        // Get the current character
                        char c = currentLine.charAt(i);
                        // Check if the character is a double quote
                        if (c == '"') {
                            // If already inside quotes
                            if (inQuotes) {
                                // Check if the next character is also a double quote (escaped quote)
                                if (i + 1 < currentLine.length() && currentLine.charAt(i + 1) == '"') {
                                    // Append the escaped double quote to the current field
                                    currentField.append('"');
                                    // Skip the next character as it's already processed
                                    i++;
                                } else {
                                    // End of the quoted field
                                    inQuotes = false;
                                }
                            } else {
                                // Start of a quoted field
                                inQuotes = true;
                            }
                            // Check if the character is a comma and not inside quotes
                        } else if (c == ',' && !inQuotes) {
                            // Add the current field to the list of fields
                            fields.add(currentField.toString());
                            // Reset the current field for the next one
                            currentField.setLength(0);
                        } else {
                            // Append the character to the current field
                            currentField.append(c);
                        }
                    }
                    // Add the last field to the list of fields
                    fields.add(currentField.toString());
                    // Convert the list of fields to an array and add it to the data list
                    data.add(fields.toArray(new String[0]));
                    // Reset the current line for the next iteration
                    currentLine.setLength(0);
                } else {
                    // If still inside quotes, append a newline and continue reading the next line
                    currentLine.append("\n");
                }
            }
        }
        // Return the parsed CSV data
        return data;
    }
    /**
     * Loads movie data from a specified CSV file.
     * Parses the CSV file, skips the header row, and creates Movie objects for each data row.
     * Adds genre information to each movie and stores the movies in the movies TreeSet.
     *
     * @param filePath The path to the CSV file containing movie data.
     */
    public void loadFile(String filePath){
        try {
            // Parse the CSV file into a list of string arrays
            List<String[]> csvData = parseCsv(filePath);
            // Remove the header row from the CSV data
            csvData.remove(0);
            // Iterate through each row of the CSV data
            for (String[] row : csvData) {
                // Extract the movie ID from the fourth column and convert it to an integer
                int id = getId(row[3]);
                // Extract the movie title from the eighteenth column
                String title = row[17];
                // Extract the release date from the twelfth column
                String date = row[11];
                // Create a new Movie object with the extracted information
                Movie movie = new Movie(title, id, date);
                // Parse and add genre information to the movie from the second column
                handleGenre(movie, row[1]);
                // Add the movie to the TreeSet
                movies.add(movie);
            }
        } catch (Exception e) {
            // Print the stack trace if an exception occurs during loading
            e.printStackTrace();
        }
    }

    /**
     * Loads extra movie information such as cast and crew from a specified CSV file.
     * Parses the CSV file, skips the header row, and updates existing Movie objects
     * with cast and crew information.
     *
     * @param filePath The path to the CSV file containing extra movie information.
     */
    public void loadExtraFile(String filePath){
        try {
            // Parse the CSV file into a list of string arrays
            List<String[]> csvData = parseCsv(filePath);
            // Remove the header row from the CSV data
            csvData.remove(0);
            // Iterate through each row of the CSV data
            for (String[] row : csvData) {
                // Extract the movie ID from the first column and convert it to an integer
                int id = getId(row[0]);
                // Find the movie in the TreeSet using the extracted ID
                Movie movie = movies.floor(new Movie(id));
                // Parse and add cast information to the movie from the third column
                handleCast(movie, row[2]);
                // Parse and add crew information to the movie from the fourth column
                handleCrew(movie, row[3]);
            }
        } catch (Exception e) {
            // Print the stack trace if an exception occurs during loading
            e.printStackTrace();
        }
    }

    /**
     * Parses a JSON string containing cast information and adds it to the specified movie.
     *
     * @param movie The Movie object to which cast information will be added.
     * @param json The JSON string containing cast information.
     */
    public void handleCast(Movie movie, String json){
        // Parse the JSON string into an array of Stuff objects representing cast members
        Stuff[] list = parseJson.parseCast(json);
        // Iterate through each cast member and add them to the movie
        for (Stuff stuff : list) {
            movie.addStuff(stuff);
            if(!stuffs.contains(stuff)){
                stuffs.add(stuff);
            }
        }
    }

    /**
     * Parses a JSON string containing crew information and adds it to the specified movie.
     *
     * @param movie The Movie object to which crew information will be added.
     * @param json The JSON string containing crew information.
     */
    public void handleCrew(Movie movie, String json){
        // Parse the JSON string into an array of Stuff objects representing crew members
        Stuff[] list = parseJson.parseCrew(json);
        // Iterate through each crew member and add them to the movie
        for (Stuff stuff : list) {
            movie.addStuff(stuff);
            if(!stuffs.contains(stuff)){
                stuffs.add(stuff);
            }
        }
    }

    /**
     * Parses a JSON string containing genre information and adds it to the specified movie.
     *
     * @param movie The Movie object to which genre information will be added.
     * @param json The JSON string containing genre information.
     */
    public void handleGenre(Movie movie, String json){
        try {
            // Parse the JSON string into an array of Genre objects
            Genre[] list = parseJson.parseGenre(json);
            // Iterate through each genre and add it to the movie
            for (Genre genre : list) {
                movie.addGenre(genre);
            }
        } catch (Exception e) {
            // Print the stack trace if an exception occurs during parsing
            e.printStackTrace();
        }
    }

    /**
     * Converts a string representation of an ID to an integer.
     * Returns -1 if the conversion fails.
     *
     * @param id The string representation of the ID.
     * @return The integer representation of the ID, or -1 if conversion fails.
     */
    public int getId(String id){
        try {
            // Convert the string to an integer
            return Integer.parseInt(id);
        } catch (Exception e) {
            // Return -1 if the conversion fails
            return -1;
        }
    }

    /**
     * Retrieves the set of all Movie objects loaded by the CSVReader.
     *
     * @return A Set containing all Movie objects.
     */
    public Set<Movie> readMovies(){
        return movies;
    }

    /**
     * The main method for testing the CSVReader class.
     * Creates an instance of CSVReader and loads movie data from two CSV files.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Create a new instance of CSVReader
        CSVReader reader = new CSVReader();
        // Load movie information from the main CSV file
        reader.loadFile("data/tmdb_5000_movies.csv");
        // Load extra information such as cast and crew from the additional CSV file
        reader.loadExtraFile("data/tmdb_5000_credits.csv");
    }
}
