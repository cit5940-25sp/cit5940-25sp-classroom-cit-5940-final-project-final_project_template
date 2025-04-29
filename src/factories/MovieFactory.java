package factories;

import models.Movie;
import models.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工厂类，负责创建电影对象
 */
public class MovieFactory {
    
    /**
     * 创建一个新的电影对象
     */
    public static Movie createMovie(String title, int id, int releaseYear, Set<String> genre, 
                                  List<Tuple<String, Integer>> cast, List<Tuple<String, Integer>> crew) {
        return new Movie(title, id, releaseYear, genre, cast, crew);
    }
    
    /**
     * 从数据映射创建电影对象
     */
    public static Movie createMovieFromData(Map<String, Object> data) {
        // 这里可以实现从数据映射（如JSON或数据库结果）创建电影对象的逻辑
        // 目前是一个简单的实现，实际应用中可以根据需要扩展
        String title = (String) data.get("title");
        int id = (int) data.get("id");
        int releaseYear = (int) data.get("releaseYear");
        
        @SuppressWarnings("unchecked")
        Set<String> genre = (Set<String>) data.get("genre");
        
        @SuppressWarnings("unchecked")
        List<Tuple<String, Integer>> cast = (List<Tuple<String, Integer>>) data.get("cast");
        
        @SuppressWarnings("unchecked")
        List<Tuple<String, Integer>> crew = (List<Tuple<String, Integer>>) data.get("crew");
        
        return new Movie(title, id, releaseYear, genre, cast, crew);
    }
}
