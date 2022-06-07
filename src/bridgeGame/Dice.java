package bridgeGame;

import javax.swing.*;

public final class Dice {
    // 게임 화면 상의 주사위 이미지
    private final JLabel[] images;

    public Dice() {
        // 1부터 6까지의 주사위 이미지 설정
        images = new JLabel[6];
        for (int i = 0; i < images.length; i++) {
            images[i] = new JLabel(new ImageIcon("src/resources/dice" + (i + 1) + ".png"));
            images[i].setBounds(
                    1350,
                    175,
                    images[i].getIcon().getIconWidth(),
                    images[i].getIcon().getIconHeight()
            );

            // 주사위를 굴리기 전에는 주사위 이미지가 보이지 않게 함.
            images[i].setVisible(false);
        }
    }

    // 주사위를 굴려 1부터 6까지의 값 중에서 임의의 값을 반환한다.
    // 이와 동시에 해당 값에 해당하는 주사위 이미지를 보이게 한다.
    public int rollDice() {
        int num = (int) (Math.random() * 6) + 1;
        images[num - 1].setVisible(true);
        return num;
    }

    public JLabel[] getImages() {
        return images;
    }
}
