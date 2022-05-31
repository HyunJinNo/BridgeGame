package bridgeGame;

import javax.swing.*;
import java.awt.*;

public class Card {
    private int num;
    private JLabel numLabel;

    public Card() {
        num = 0;
        Font font = new Font("Default", Font.BOLD, 30);
        numLabel = new JLabel(String.valueOf(num));
        numLabel.setFont(font);
        numLabel.setForeground(Color.WHITE);
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

    public JLabel getNumLabel() {
        return numLabel;
    }
}
