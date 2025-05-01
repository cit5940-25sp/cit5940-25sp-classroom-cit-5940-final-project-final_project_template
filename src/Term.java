import java.util.Comparator;

public class Term {
    private final String term;
    private long weight;

    public Term(String term, long weight) {
        if (term == null || weight < 0) {
            throw new IllegalArgumentException("Invalid term or weight.");
        }
        this.term = term;
        this.weight = weight;
    }

    public String getTerm() {
        return term;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative.");
        }
        this.weight = weight;
    }

    public static Comparator<Term> byReverseWeightOrder() {
        return (a, b) -> Long.compare(b.weight, a.weight);
    }

    @Override
    public String toString() {
        return String.format("%d\t%s", weight, term);
    }
}
