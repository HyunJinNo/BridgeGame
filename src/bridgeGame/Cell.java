package bridgeGame;

import javax.swing.*;

public final class Cell {
    // Cell 의 x 좌표, y 좌표
    private int x;
    private int y;

    // Cell 종류를 나타내는 변수
    private final String type;

    // 해당 Cell 에서 이동 가능한 방향을 나타내는 변수
    private final String[] directions;

    // 게임 화면 상의 Cell 이미지
    private final JLabel image;

    public Cell(int x, int y, String type, String[] directions, JLabel image) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.directions = directions;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public String[] getDirections() {
        return directions;
    }

    public JLabel getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
