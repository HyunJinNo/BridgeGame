package bridgeGame;

public class Main {
    public static void main(String[] args) {
        TitleView titleView = new TitleView();
        GameView gameView = new GameView();
        new Controller(titleView, gameView);
    }
}
