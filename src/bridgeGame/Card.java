package bridgeGame;

import javax.swing.*;

public class Card {
    private int num;
    private static final ImageIcon cardImage = new ImageIcon("src/resources/card");

    public Card() {
        num = 0;
    }

    public int getNum() {
        return num;
    }

    public void returnOneCard() {
        if (num > 0) {
            num--;
        }
    }

    public void getOneCard() {
        num++;
    }
}
