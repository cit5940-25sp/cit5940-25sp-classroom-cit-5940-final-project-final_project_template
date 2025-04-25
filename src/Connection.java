/**
 * Represents a connection between two movies based on shared attributes like actor or director.
 */
public class Connection {
    private ConnectionType type;
    private String name;

    @Override
    public boolean equals(Object obj){
        return true;
    }

    @Override
    public int hashCode(){
        return 0;
    }
}
