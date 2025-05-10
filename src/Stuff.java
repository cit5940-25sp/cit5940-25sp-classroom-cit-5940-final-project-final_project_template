/**
 * Represents a stuff item with a name and an ID.
 * This class implements the Comparable interface to allow sorting of Stuff objects.
 */
public class Stuff implements Comparable<Stuff>{
    // The name of the stuff item
    private String name;
    // The unique identifier of the stuff item
    private int id;

    public Stuff(String name){
        this.name = name;
    }
    /**
     * Retrieves the job associated with the stuff item.
     * Currently, this method always returns an empty string.
     *
     * @return an empty string
     */
    public String getJob(){
        return "";
    }

    /**
     * Constructs a new Stuff object with the specified name and ID.
     *
     * @param name the name of the stuff item
     * @param id the unique identifier of the stuff item
     */
    public Stuff(String name, int id){
        this.name = name;
        this.id = id;
    }

    /**
     * Retrieves the name of the stuff item.
     *
     * @return the name of the stuff item
     */
    public String getName(){
        return name;
    }

    /**
     * Compares this stuff item with another stuff item for order.
     * Returns a negative integer, zero, or a positive integer as this stuff item's ID
     * is less than, equal to, or greater than the specified stuff item's ID.
     *
     * @param o the stuff item to be compared
     * @return a negative integer, zero, or a positive integer as this stuff item's ID
     *         is less than, equal to, or greater than the specified stuff item's ID
     */
    @Override
    public int compareTo(Stuff o) {
        return Integer.compare(id, o.id);
    }

    /**
     * Returns a string representation of the stuff item, which is its name.
     *
     * @return the name of the stuff item
     */
    @Override
    public String toString(){
        return name;
    }
}
