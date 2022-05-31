package bridgeGame;

import javax.swing.*;

public final class Cell {
    private int x;
    private int y;
    private final String type;
    private final String[] directions;
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
