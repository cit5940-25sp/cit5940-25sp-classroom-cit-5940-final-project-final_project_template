public class Player {
    private String name;
    private Links links;

    // Create a new player with the given name and an empty link path
    public Player(String name) {
        this.name = name;
        links = new Links();
    }
    public boolean play(Movie movie){
        if(links.isEmpty()){
            links.setCurrentMovie(movie);
            return true;
        }
        return links.addLink(movie);
    }
}