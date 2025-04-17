import java.util.List;

// Finds valid movie connections in game (model)
public class ConnectionValidator implements IConnectionValidator {
    @Override
    public boolean isValidConnection(IMovie movie1, IMovie movie2) {
        return false;
    }

    @Override
    public List<String> getSharedConnections(IMovie movie1, IMovie movie2) {
        return List.of();
    }

    @Override
    public int getUsageCount(String connection) {
        return 0;
    }

    @Override
    public void recordConnectionUse(List<String> connections) {

    }

    @Override
    public void resetUsageCount() {

    }
}
