package bridgeGame;

public final class Player {
    private int x;
    private int y;
    private int score;

    public Player() {
        x = 0;
        y = 0;
        score = 0;
    }

    public Player(int score) {
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void plusScore(int plus) {
        score += plus;
    }
}
