import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import factories.ServiceFactory;
import services.MovieService;
import utils.DataLoader;
import models.Movie;

public class MovieIndexer {
    private Autocomplete autocomplete;
    private MovieService movieService;

    public MovieIndexer() {
        autocomplete = new Autocomplete();
        movieService = ServiceFactory.getMovieService();
    }

    // 使用DataLoader加载电影数据
    public void loadMoviesFromCSV(String csvFilePath) {
        try {
            // 使用DataLoader加载电影数据
            DataLoader dataLoader = new DataLoader(movieService);
            dataLoader.loadMoviesFromCsv(csvFilePath);
            
            // 将加载的电影添加到Autocomplete中
            for (Movie movie : movieService.getAllMovies()) {
                autocomplete.insert(movie);
            }
        } catch (IOException e) {
            System.err.println("读取CSV文件时出错: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("处理电影数据时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 搜索电影
    public List<Movie> searchMovies(String prefix) {
        return autocomplete.search(prefix);
    }

    // 设置返回结果的数量
    public List<Movie> searchMovies(String prefix, int limit) {
        return autocomplete.search(prefix, limit);
    }

    public static void main(String[] args) {
        MovieIndexer indexer = new MovieIndexer();
        Scanner scanner = new Scanner(System.in);

        // 加载电影数据
        System.out.print("请输入movies.csv文件的路径: ");
        String csvPath = scanner.nextLine();
        indexer.loadMoviesFromCSV(csvPath);

        // 交互式搜索
        while (true) {
            System.out.print("\n请输入要搜索的电影前缀（输入'quit'退出）: ");
            String prefix = scanner.nextLine().trim();
            
            if (prefix.equalsIgnoreCase("quit")) {
                break;
            }

            List<Movie> results = indexer.searchMovies(prefix, 10); // 返回最多10个结果
            if (results.isEmpty()) {
                System.out.println("没有找到以 \"" + prefix + "\" 开头的电影。");
            } else {
                System.out.println("找到" + results.size() + "部电影：");
                int count = 1;
                for (Movie movie : results) {
                    System.out.println(count + ". " + movie.getTitle() + " (" + movie.getReleaseYear() + ")");
                    if (!movie.getGenre().isEmpty()) {
                        System.out.println("   类型: " + String.join(", ", movie.getGenre()));
                    }
                    count++;
                }
            }
        }

        scanner.close();
        System.out.println("程序已退出。");
    }
} 