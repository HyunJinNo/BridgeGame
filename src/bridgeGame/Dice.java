package bridgeGame;

import javax.swing.*;

public final class Dice {
    private JLabel[] images;

    public Dice() {
        images = new JLabel[6];
        for (int i = 0; i < images.length; i++) {
            images[i] = new JLabel(new ImageIcon("src/resources/dice" + (i + 1) + ".png"));
            images[i].setBounds(1350, 175, images[i].getIcon().getIconWidth(), images[i].getIcon().getIconHeight());
            images[i].setVisible(false);
        }
    }

    public int rollDice() {
        int num = (int) (Math.random() * 6) + 1;
        images[num - 1].setVisible(true);
        return num;
    }

    public JLabel[] getImages() {
        return images;
    }
}
