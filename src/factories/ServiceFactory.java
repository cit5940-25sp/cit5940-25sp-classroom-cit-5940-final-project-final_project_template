package factories;

import services.ClientService;
import services.GameService;
import services.MovieService;

/**
 * 工厂类，负责创建和管理服务实例
 */
public class ServiceFactory {
    private static ClientService clientService;
    private static MovieService movieService;
    private static GameService gameService;
    
    /**
     * 获取ClientService实例
     */
    public static ClientService getClientService() {
        if (clientService == null) {
            clientService = new ClientService();
        }
        return clientService;
    }
    
    /**
     * 获取MovieService实例
     */
    public static MovieService getMovieService() {
        if (movieService == null) {
            movieService = new MovieService();
        }
        return movieService;
    }
    
    /**
     * 获取GameService实例
     */
    public static GameService getGameService() {
        if (gameService == null) {
            ClientService clientSvc = getClientService();
            MovieService movieSvc = getMovieService();
            gameService = new GameService(clientSvc, movieSvc);
        }
        return gameService;
    }
}
