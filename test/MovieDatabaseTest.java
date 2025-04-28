public class MovieDatabaseTest {
    public static void main(String[] args) throws Exception {
        MovieDatabase db = new MovieDatabase();
        db.loadFromCSV("data/movies.csv", "data/credits.csv");

        // Test autocomplete
        System.out.println("Autocomplete for 'Ava': " + db.getAutocompleteSuggestions("Ava", 5));

        // Test movie lookup
        Movie avatar = db.findMovie("Avatar");
        System.out.println("Found movie: " + (avatar != null ? avatar.getTitle() : "not found"));

        // Test connection validation (example: Avatar and another movie)
        Movie another = db.findMovie("Pirates of the Caribbean: At World's End");
        if (avatar != null && another != null) {
            Connection conn = db.validateConnection(avatar, another);
            System.out.println("Connection: " + (conn != null ? conn.getConnectionType() + " via " + conn.getConnector().getName() : "none"));
        }
    }
}
