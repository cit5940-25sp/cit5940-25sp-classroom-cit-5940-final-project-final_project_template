import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

public class Autocomplete {
    // Trie 节点类
    private static class TrieNode {
        private Map<Character, TrieNode> children;
        private boolean isEndOfWord;
        private Movie movie;

        public TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
            movie = null;
        }
    }

    private TrieNode root;

    public Autocomplete() {
        root = new TrieNode();
    }

    // 插入电影到 Trie
    public void insert(Movie movie) {
        TrieNode current = root;
        String lowerTitle = movie.getTitle().toLowerCase();
        
        for (int i = 0; i < lowerTitle.length(); i++) {
            char c = lowerTitle.charAt(i);
            
            if (!current.children.containsKey(c)) {
                current.children.put(c, new TrieNode());
            }
            current = current.children.get(c);
        }
        
        current.isEndOfWord = true;
        current.movie = movie;
    }

    // 根据前缀搜索匹配的电影
    public List<Movie> search(String prefix) {
        return search(prefix, 1); // 默认返回1个结果
    }

    // 根据前缀搜索匹配的电影，限制返回数量
    public List<Movie> search(String prefix, int limit) {
        List<Movie> results = new ArrayList<>();
        TrieNode current = root;
        String lowerPrefix = prefix.toLowerCase();

        // 先找到前缀的最后一个节点
        for (int i = 0; i < lowerPrefix.length(); i++) {
            char c = lowerPrefix.charAt(i);
            
            if (!current.children.containsKey(c)) {
                return results; // 如果没有找到前缀，返回空列表
            }
            current = current.children.get(c);
        }

        // 从该节点开始，收集所有可能的电影
        collectMovies(current, results);

        // 按匹配度排序并限制返回数量
        results.sort(new MovieComparator(prefix));
        return results.subList(0, Math.min(limit, results.size()));
    }

    // 递归收集所有可能的电影
    private void collectMovies(TrieNode node, List<Movie> results) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord && node.movie != null) {
            results.add(node.movie);
        }

        for (TrieNode child : node.children.values()) {
            collectMovies(child, results);
        }
    }

    // 电影比较器，用于按匹配度排序
    private static class MovieComparator implements Comparator<Movie> {
        private final String prefix;

        public MovieComparator(String prefix) {
            this.prefix = prefix.toLowerCase();
        }

        @Override
        public int compare(Movie m1, Movie m2) {
            String title1 = m1.getTitle().toLowerCase();
            String title2 = m2.getTitle().toLowerCase();

            // 如果前缀完全匹配，优先返回
            if (title1.startsWith(prefix) && !title2.startsWith(prefix)) {
                return -1;
            }
            if (!title1.startsWith(prefix) && title2.startsWith(prefix)) {
                return 1;
            }

            // 如果都完全匹配或都不完全匹配，按标题长度排序
            // 标题更短的优先（通常更接近用户想要的结果）
            return Integer.compare(title1.length(), title2.length());
        }
    }

    // public static void main(String[] args) {
    //     Autocomplete autocomplete = new Autocomplete();

    //     // 创建一些测试用的电影数据
    //     Set<String> genres1 = new HashSet<>();
    //     genres1.add("Action");
    //     genres1.add("Adventure");
    //     genres1.add("Sci-Fi");

    //     Set<String> genres2 = new HashSet<>();
    //     genres2.add("Drama");
    //     genres2.add("Crime");

    //     Set<String> genres3 = new HashSet<>();
    //     genres3.add("Action");
    //     genres3.add("Thriller");

    //     // 创建测试用的演员和工作人员列表
    //     List<Tuple<String, Integer>> cast1 = new ArrayList<>();
    //     cast1.add(new Tuple<>("Tom Cruise", 1));
    //     cast1.add(new Tuple<>("Emily Blunt", 2));

    //     List<Tuple<String, Integer>> crew1 = new ArrayList<>();
    //     crew1.add(new Tuple<>("Christopher McQuarrie", 101));
    //     crew1.add(new Tuple<>("Tom Cruise", 102));

    //     List<Tuple<String, Integer>> cast2 = new ArrayList<>();
    //     cast2.add(new Tuple<>("Al Pacino", 3));
    //     cast2.add(new Tuple<>("Robert De Niro", 4));

    //     List<Tuple<String, Integer>> crew2 = new ArrayList<>();
    //     crew2.add(new Tuple<>("Martin Scorsese", 103));
    //     crew2.add(new Tuple<>("Michael Mann", 104));

    //     // 创建电影对象
    //     Movie movie1 = new Movie("Mission: Impossible - Fallout", 1, 2018, genres1, cast1, crew1);
    //     Movie movie2 = new Movie("The Irishman", 2, 2019, genres2, cast2, crew2);
    //     Movie movie3 = new Movie("The Dark Knight", 3, 2008, genres3, cast1, crew1);

    //     // 插入电影到 Trie
    //     autocomplete.insert(movie1);
    //     autocomplete.insert(movie2);
    //     autocomplete.insert(movie3);

    //     // 测试搜索功能
    //     System.out.println("测试1: 搜索 'the'");
    //     List<Movie> results1 = autocomplete.search("the");
    //     for (Movie movie : results1) {
    //         System.out.println("- " + movie.getTitle() + " (" + movie.getReleaseYear() + ")");
    //         System.out.println("  类型: " + String.join(", ", movie.getGenre()));
    //     }

    //     System.out.println("\n测试2: 搜索 'mission'");
    //     List<Movie> results2 = autocomplete.search("mission");
    //     for (Movie movie : results2) {
    //         System.out.println("- " + movie.getTitle() + " (" + movie.getReleaseYear() + ")");
    //         System.out.println("  类型: " + String.join(", ", movie.getGenre()));
    //     }

    //     System.out.println("\n测试3: 搜索 'dark'");
    //     List<Movie> results3 = autocomplete.search("dark");
    //     for (Movie movie : results3) {
    //         System.out.println("- " + movie.getTitle() + " (" + movie.getReleaseYear() + ")");
    //         System.out.println("  类型: " + String.join(", ", movie.getGenre()));
    //     }

    //     System.out.println("\n测试4: 搜索不存在的电影 'xyz'");
    //     List<Movie> results4 = autocomplete.search("xyz");
    //     if (results4.isEmpty()) {
    //         System.out.println("没有找到匹配的电影");
    //     }
    // }
} 