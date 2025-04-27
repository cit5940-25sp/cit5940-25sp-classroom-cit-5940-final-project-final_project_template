import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;

// Finds valid movie connections in game (model)
public class ConnectionValidator implements IConnectionValidator {

    private Map<String, Integer> usageCount;
    private final int MAX_USAGE_PER_PERSON = 3;

    public ConnectionValidator() {
        this.usageCount = new HashMap<>();
    }

    public boolean isValidConnection(IMovie movie1, IMovie movie2) {
        List<String> shared = getSharedConnections(movie1, movie2);
        for (String person : shared) {
            if (getUsageCount(person) < MAX_USAGE_PER_PERSON) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getSharedConnections(IMovie movie1, IMovie movie2) {
        Set<String> contributorsA = movie1.getAllContributors();
        Set<String> contributorsB = movie2.getAllContributors();
        List<String> shared = new ArrayList<>();

        for (String person : contributorsA) {
            if (contributorsB.contains(person)) {
                shared.add(person);
            }
        }
        return shared;
    }

    @Override
    public int getUsageCount(String connection) {
        return usageCount.getOrDefault(connection, 0);
    }

    @Override
    public void recordConnectionUse(List<String> connections) {
        for (String name: connections) {
            int count = usageCount.getOrDefault(name, 0);
            usageCount.put(name, count + 1);
        }
    }

    @Override
    public void resetUsageCount() {
        usageCount.clear();
    }
}
