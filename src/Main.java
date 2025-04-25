public class Main {
    public static void main(String[] args) {
        TMDBClient client = new TMDBClient();

        Movie movie = client.fetchMovieByTitle("Inception");
        if (movie != null) {
            System.out.println("✔️ 成功連線！電影名稱：" + movie.getTitle());
            System.out.println("上映年份：" + movie.getYear());
            System.out.println("導演：" + movie.getDirectors());
            System.out.println("演員：" + movie.getActors());
            System.out.println("類型：" + movie.getGenres());
        } else {
            System.out.println("❌ 無法取得電影資訊，可能是 API Key 錯誤或未連線。");
        }
    }
}
