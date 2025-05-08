public class Player {
    private String name;
    private Links links;

    // Create a new player with the given name and an empty link path
    public Player(String name) {
        this.name = name;
        links = new Links();
    }
}