package bridgeGame;

import javax.swing.*;

public final class Player {
    private int x;
    private int y;
    private int score;
    private final JLabel image;
    private final Card card;

    public Player(JLabel image) {
        x = 0;
        y = 0;
        score = 0;
        this.image = image;
        card = new Card();
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

    public JLabel getImage() {
        return image;
    }

    public Card getCard() {
        return card;
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
