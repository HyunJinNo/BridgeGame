package bridgeGame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public final class TitleView extends JPanel {
    // TitleView 의 Width
    private static final int WIDTH = 1500;

    // TitleView 의 Height
    private static final int HEIGHT = 900;

    // TitleView 의 2개의 버튼: PLAY, LOAD
    JButton[] buttons;

    JLabel filenameLabel;

    public TitleView() {
        setLayout(null);

        // 버튼 텍스트, 폰트 크기, 버튼 위치 등 설정.
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

        // 로드할 지도 데이터 파일명을 표시.

        filenameLabel = new JLabel("File: default.map");
        filenameLabel.setBounds(25, 0, 500, 100);
        filenameLabel.setForeground(Color.WHITE);
        filenameLabel.setFont(font);
        add(filenameLabel);

        // TitleView 의 background 이미지 설정.
        JLabel titleImage = new JLabel(new ImageIcon("src/resources/title.png"));
        titleImage.setBounds(0, 0, 1500, 900);
        add(titleImage);

        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
}
