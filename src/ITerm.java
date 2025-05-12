import java.util.Comparator;

/**
 * @author ericfouh
 */
public interface ITerm extends Comparable<ITerm> {

    /**
     * Compares the two terms in descending order by weight.
     * 
     * @return comparator Object
     */
    public static Comparator<ITerm> byReverseWeightOrder() {
        return (a, b) -> Long.compare(b.getWeight(), a.getWeight());
    }


    /**
     * Compares the two terms in lexicographic order but using only the first r
     * characters of each query.
     * 
     * @param r
     * @return comparator Object
     */
    public static Comparator<ITerm> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("r cannot be negative.");
        }

        return (a, b) -> {
            String aPrefix;
            String bPrefix;

            if (a.getTerm().length() < r) {
                aPrefix = a.getTerm();
            } else {
                aPrefix = a.getTerm().substring(0, r);
            }

            if (b.getTerm().length() < r) {
                bPrefix = b.getTerm();
            } else {
                bPrefix = b.getTerm().substring(0, r);
            }

            return aPrefix.compareTo(bPrefix);
        };
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(ITerm that);


    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString();

    // Required getters.
    public long getWeight();
    public String getTerm();

    // Required setters (mostly for autograding purposes)
    public void setWeight(long weight);
    public String setTerm(String term);

}
