import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieIndexer {
    private Autocomplete autocomplete;

    public MovieIndexer() {
        autocomplete = new Autocomplete();
    }

    // 解析演员和工作人员列表
    private List<Tuple<String, Integer>> parsePeopleList(String peopleStr) {
        List<Tuple<String, Integer>> people = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\('([^']*)', (\\d+)\\)");
        Matcher matcher = pattern.matcher(peopleStr);
        
        while (matcher.find()) {
            String name = matcher.group(1);
            int id = Integer.parseInt(matcher.group(2));
            people.add(new Tuple<>(name, id));
        }
        return people;
    }

    // 解析类型列表
    private Set<String> parseGenres(String genresStr) {
        Set<String> genres = new HashSet<>();
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(genresStr);
        
        while (matcher.find()) {
            genres.add(matcher.group(1));
        }
        return genres;
    }

    // 从CSV文件加载电影数据
    public void loadMoviesFromCSV(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            // 跳过表头
            br.readLine();
            
            String line;
            while ((line = br.readLine()) != null) {
                // 使用逗号分割，但保留引号内的逗号
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length >= 6) {
                    try {
                        // 解析各个字段
                        int id = Integer.parseInt(parts[2]);
                        int releaseYear = Integer.parseInt(parts[3]);
                        String title = parts[4].replaceAll("^\"|\"$", "");
                        Set<String> genres = parseGenres(parts[1]);
                        List<Tuple<String, Integer>> cast = parsePeopleList(parts[5]);
                        List<Tuple<String, Integer>> crew = parsePeopleList(parts[6]);

                        // 创建Movie对象
                        Movie movie = new Movie(title, id, releaseYear, genres, cast, crew);
                        autocomplete.insert(movie);
                    } catch (NumberFormatException e) {
                        System.err.println("解析数字时出错: " + e.getMessage());
                    }
                }
            }
            System.out.println("电影数据加载完成！");
        } catch (IOException e) {
            System.err.println("读取CSV文件时出错: " + e.getMessage());
        }
    }

    // 搜索电影
    public List<Movie> searchMovies(String prefix) {
        return autocomplete.search(prefix);
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

            List<Movie> results = indexer.searchMovies(prefix);
            if (results.isEmpty()) {
                System.out.println("没有找到以 \"" + prefix + "\" 开头的电影。");
            } else {
                System.out.println("找到以下电影：");
                for (Movie movie : results) {
                    System.out.println("- " + movie.getTitle() + " (" + movie.getReleaseYear() + ")");
                    System.out.println("  类型: " + String.join(", ", movie.getGenre()));
                }
            }
        }

        scanner.close();
        System.out.println("程序已退出。");
    }
} 