/**
 * Represents a connection between two movies based on shared attributes like actor or director.
 */
public class Connection {
    private String personName;
    private ConnectionType type;

    public Connection(String personName, ConnectionType type) {
        this.personName = personName;
        this.type = type;
    }

    public String getPersonName() {
        return personName;
    }

    public ConnectionType getType() {
        return type;
    }

    // Optional: override equals() and hashCode() if you need to use this in Sets or Maps
}
