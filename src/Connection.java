/**
 * Represents a connection between two movies based on shared attributes like actor or director.
 */
public class Connection {
    private ConnectionType type;
    private String name;

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();
}
