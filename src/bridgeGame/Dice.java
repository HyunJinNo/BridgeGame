package bridgeGame;

import javax.swing.*;

public final class Dice {
    private JLabel[] images;

    public Dice() {
        images = new JLabel[16];
        for (int i = 0; i < 16; i++) {
            images[i] = new JLabel(new ImageIcon("src/resources/dice" + (i + 1)));
            images[i].setBounds(550, 250, images[i].getIcon().getIconWidth(), images[i].getIcon().getIconHeight());
            images[i].setVisible(false);
        }
    }

    public int rollDice() {
        for (int i = 0; i < 5; i++) {
            for (JLabel image : images) {
                image.setVisible(true);
                image.setVisible(false);
            }
        }

        int num = (int) (Math.random() * 6) + 1;

        images[num].setVisible(true);

        return num;
    }
}
