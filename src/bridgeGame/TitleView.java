package bridgeGame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public final class TitleView extends JPanel {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 900;
    JButton[] buttons;
    private final JLabel titleImage;

    public TitleView() {
        setLayout(null);

        buttons = new JButton[2];
        Font font = new Font("Default", Font.BOLD, 30);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorder(new BevelBorder(BevelBorder.RAISED));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(font);
            add(buttons[i]);
        }
        buttons[0].setText("PLAY");
        buttons[1].setText("LOAD");

        buttons[0].setBounds(1250, 675, 200, 75);
        buttons[1].setBounds(1250, 775, 200, 75);

        titleImage = new JLabel();
        titleImage.setIcon(new ImageIcon("src/resources/title.png"));
        titleImage.setBounds(0, 0, 1500, 900);
        add(titleImage);

        setBackground(Color.WHITE);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
}
