import java.util.Objects;

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

    // Two Connection objects are considered equal iff both personName and type match
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // same object reference
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // null or different class
        }
        Connection that = (Connection) o;
        return Objects.equals(personName, that.personName) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, type);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", personName, type);
    }
}
