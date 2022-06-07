package bridgeGame;

import javax.swing.*;
import java.awt.*;

public class Card {
    // 카드의 수
    private int num;
    
    // 게임 화면 상의 카드 이미지
    private final JLabel numLabel;

    public Card() {
        this.num = 0;
        Font font = new Font("Default", Font.BOLD, 30);
        numLabel = new JLabel(String.valueOf(num));
        numLabel.setFont(font);
        numLabel.setForeground(Color.WHITE);
    }

    public int getNum() {
        return num;
    }

    // 카드 1장을 반납하는 메소드
    public void returnOneCard() {
        if (num > 0) {
            num--;
        }
    }

    // 다리를 건넜을 때 카드 1장을 받는 메소드
    public void getOneCard() {
        num++;
    }

    public JLabel getNumLabel() {
        return numLabel;
    }
}
