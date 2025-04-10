import java.util.Set;

public class ConnectionValidator {

    /**
     * 判断两部电影是否有合法的连接关系。
     * 合法连接包括至少一个相同的：
     * - 演员（actor）
     * - 导演（director）
     * - 编剧（writer）
     * - 摄影师（cinematographer）
     * - 作曲家（composer）
     */
    public boolean isValidConnection(Movie movie1, Movie movie2) {
        if (movie1 == null || movie2 == null) return false;

        // 1. 判断是否有相同演员
        if (hasSharedElement(movie1.getActors(), movie2.getActors())) return true;

        // 2. 判断是否有相同导演
        if (isNonEmptyMatch(movie1.getDirector(), movie2.getDirector())) return true;

        // 3. 判断是否有相同编剧
        if (isNonEmptyMatch(movie1.getWriter(), movie2.getWriter())) return true;

        // 4. 判断是否有相同摄影师
        if (isNonEmptyMatch(movie1.getCinematographer(), movie2.getCinematographer())) return true;

        // 5. 判断是否有相同作曲家
        if (isNonEmptyMatch(movie1.getComposer(), movie2.getComposer())) return true;

        return false;
    }

    /**
     * 判断两个字符串是否非空且相等（忽略大小写）
     */
    private boolean isNonEmptyMatch(String s1, String s2) {
        return s1 != null && s2 != null && !s1.isEmpty() && !s2.isEmpty()
                && s1.equalsIgnoreCase(s2);
    }

    /**
     * 判断两个集合是否有至少一个相同元素
     */
    private boolean hasSharedElement(Set<String> set1, Set<String> set2) {
        for (String item : set1) {
            if (set2.contains(item)) {
                return true;
            }
        }
        return false;
    }
}
