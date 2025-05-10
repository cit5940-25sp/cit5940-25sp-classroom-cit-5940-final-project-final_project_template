/**
 * Represents a genre with an ID and a name.
 * This class implements the Comparable interface to allow sorting of Genre objects.
 */
public class Genre implements Comparable<Genre>{
    // Unique identifier for the genre
    private int id;
    // Name of the genre
    private String name;

    /**
     * Constructs a new Genre object with the specified name and ID.
     *
     * @param name the name of the genre
     * @param id the unique identifier for the genre
     */
    public Genre(String name, int id){
        this.name = name;
        this.id = id;
    }

    /**
     * Retrieves the name of the genre.
     *
     * @return the name of the genre
     */
    public String getName(){
        return name;
    }


    /**
     * Compares this genre with another genre for order.
     * Returns a negative integer, zero, or a positive integer as this genre's ID
     * is less than, equal to, or greater than the specified genre's ID.
     *
     * @param other the genre to be compared
     * @return a negative integer, zero, or a positive integer as this genre's ID
     *         is less than, equal to, or greater than the specified genre's ID
     */
    @Override
    public int compareTo(Genre other){
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object other){
        Genre genre = (Genre) other;
        return compareTo(genre) == 0;
    }

    @Override
    public int hashCode() {
        return id;
    }
    /**
     * Returns a string representation of the genre, which is its name.
     *
     * @return the name of the genre
     */
    @Override
    public String toString() {
        return name;
    }
}