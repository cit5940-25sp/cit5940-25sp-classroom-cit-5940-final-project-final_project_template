/**
 * Represents a crew member, which is a specific type of Stuff.
 * This class extends the Stuff class and adds functionality related to a crew member's job.
 */
public class Crew extends Stuff{
    // The job title of the crew member
    private String job;

    /**
     * Constructs a new Crew object.
     *
     * @param name The name of the crew member.
     * @param id The unique identifier of the crew member.
     * @param job The job title of the crew member.
     */
    public Crew(String name, int id, String job){
        // Call the constructor of the superclass (Stuff)
        super(name, id);
        // Initialize the job title of the crew member
        this.job = job;
    }

    /**
     * Retrieves the job title of the crew member.
     *
     * @return The job title of the crew member.
     */
    public String getJob(){
        return job;
    }

    /**
     * Sets the job title of the crew member.
     *
     * @param job The new job title to be set for the crew member.
     */
    public void setJob(String job){
        this.job = job;
    }

    /**
     * Returns a string representation of the Crew object.
     * The string starts with the job title followed by the superclass's string representation.
     *
     * @return A string representation of the Crew object.
     */
    @Override
    public String toString() {
        // Concatenate the job title with the superclass's string representation
        String str = getJob() +":" + super.toString();
        return str;
    }
}
