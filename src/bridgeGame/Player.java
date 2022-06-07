package bridgeGame;

import javax.swing.*;

public final class Player {
    // 플레이어의 x 좌표, y 좌표
    private int x;
    private int y;
    
    // 플레이어의 점수
    private int score;
    
    // 게임 화면 상의 플레이어 이미지
    private final JLabel image;
    
    // 플레이어가 갖고 있는 카드
    private final Card card;
    
    // true: 게임 진행 중, false: End 에 도달하여 게임 진행이 끝난 상태
    private boolean isActive;

    public Player(JLabel image) {
        this.x = 0;
        this.y = 0;
        this.score = 0;
        this.image = image;
        this.card = new Card();
        this.isActive = true;
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

    public boolean isActive() {
        return isActive;
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

    // End 에 도달하여 게임을 끝낸 상태로 전환하는 메소드.
    public void finish() {
        isActive = false;
    }
}
