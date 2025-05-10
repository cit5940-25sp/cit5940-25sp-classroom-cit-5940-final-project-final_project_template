/**
 * Represents a cast member, which is a specific type of stuff.
 * This class extends the Stuff class and adds properties related to a cast member.
 */
public class Cast extends Stuff{
    // The name of the character played by the cast member
    private String character;
    // The order in which the cast member is listed
    private int order;

    /**
     * Constructs a new Cast object.
     *
     * @param name The name of the cast member.
     * @param id The unique identifier of the cast member.
     * @param character The name of the character played by the cast member.
     * @param order The order in which the cast member is listed.
     */
    public Cast(String name, int id, String character, int order){
        // Call the constructor of the superclass (Stuff)
        super(name, id);
        this.character = character;
        this.order = order;
    }

    /**
     * Returns a string representation of the Cast object.
     * The string starts with "Cast:" followed by the superclass's string representation.
     *
     * @return A string representation of the Cast object.
     */
    @Override
    public String toString() {
        String str = "Cast:" + super.toString();
        return str;
    }

    /**
     * Gets the job title of the cast member.
     * For Cast objects, the job title is always "Actor".
     *
     * @return The job title "Actor".
     */
    @Override
    public String getJob() {
        return "Actor";
    }
}

