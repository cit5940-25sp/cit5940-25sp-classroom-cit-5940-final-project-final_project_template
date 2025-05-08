public class CineGame {
    private GameModel model;
    private GameView view;
    private Control control;
    public CineGame(){
        view = new GameView();
        model = new GameModel();
        control = new GameControl();
        model.addObserver(view);
    }
    public void init(){
        model.initialData();
    }

    public void gameLoop(){
        ;
    }
    public static void main(String[] args) {
        CineGame game = new CineGame();
        game.init();
    }
}
