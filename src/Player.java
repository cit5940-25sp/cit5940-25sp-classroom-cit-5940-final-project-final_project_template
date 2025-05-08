public class Player {
    private String name;
    private Links links;

    // Create a new player with the given name and an empty link path
    public Player(String name) {
        this.name = name;
        links = new Links();
    }
    // Try to make a move by playing the given movie
    // If it's the first move, set it as the starting point
    // Otherwise, try to connect it to the current path
    public boolean play(Movie movie){
        if(links.isEmpty()){
            links.setCurrentMovie(movie);
            return true;
        }
        return links.addLink(movie);
    }

    // Check if the player has completed a valid path with a shared genre
    public boolean isWinner(){
        if (getGenre() != null){
            return true;
        }else{
            return false;
        }
    }

    // Return the common genre (as a string) if one exists across the player's links
    public String getGenre(){
        String str = null;
        Genre genre = links.getCommonGenre();
        if (genre != null){
            str = genre.toString();
        }
        return str;
    }

}