public class MovieTrieTest {
    public static void main(String[] args) {
        MovieTrie trie = new MovieTrie();

        // Insert some movie titles
        trie.insert("Avatar");
        trie.insert("Avengers");
        trie.insert("Avengers: Endgame");
        trie.insert("Avengers: Infinity War");
        trie.insert("Alien");
        trie.insert("Aladdin");
        trie.insert("A Beautiful Mind");

        // Test autocomplete
        System.out.println("Suggestions for 'A': " + trie.getWordsWithPrefix("A", 5));
        System.out.println("Suggestions for 'Av': " + trie.getWordsWithPrefix("Av", 5));
        System.out.println("Suggestions for 'Ave': " + trie.getWordsWithPrefix("Ave", 5));
        System.out.println("Suggestions for 'Ali': " + trie.getWordsWithPrefix("Ali", 5));
        System.out.println("Suggestions for 'B': " + trie.getWordsWithPrefix("B", 5));
        System.out.println("Suggestions for 'Z': " + trie.getWordsWithPrefix("Z", 5));
    }
}
