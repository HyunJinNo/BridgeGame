package bridgeGame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public final class Controller {
    private final TitleView titleView;
    private final GameView gameView;
    private JFrame frame;
    private JPanel panel;
    private CardLayout cardLayout;
    private Player[] players;
    private String filename = "src/bridgeGame/default.map";

    public Controller(TitleView titleView, GameView gameView) {
        this.titleView = titleView;
        this.gameView = gameView;

        EventQueue.invokeLater(() -> {
            frame = new JFrame();
            cardLayout = new CardLayout();
            panel = new JPanel(cardLayout);
            panel.add(titleView, "titleView");
            panel.add(gameView, "gameView");
            frame.add(panel);

            cardLayout.show(panel, "titleView");

            frame.setResizable(false);
            frame.pack();
            frame.setTitle("Bridge Game");
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            titleView.buttons[0].addActionListener(e -> {
                JFrame inputDialog = new JFrame();
                String num = JOptionPane.showInputDialog(inputDialog, "플레이어의 수를 입력하세요");
                if (num == null) {
                    JOptionPane.showMessageDialog(new JFrame(), "값이 입력되지 않았습니다");
                } else if (num.equals("2") || num.equals("3") || num.equals("4")) {
                    players = new Player[Integer.parseInt(num)];
                    for (int i = 0; i < players.length; i++) {
                        JLabel image = new JLabel();
                        image.setIcon(new ImageIcon("src/resources/p" + (i + 1) + ".png"));
                        players[i] = new Player(image);
                    }
                    cardLayout.show(panel, "gameView");
                    gameView.init(players, filename);
                    gameView.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "2, 3, 4중 하나의 값을 입력해주세요");
                }
            });

            titleView.buttons[1].addActionListener(e -> {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Select an map file");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("map", "map");
                jfc.addChoosableFileFilter(filter);

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    System.out.println(jfc.getSelectedFile().getPath());
                    filename = jfc.getSelectedFile().getPath();
                } else {
                    System.out.println("Nothing to load");
                }
            });

            frame.setVisible(true);
        });
    }
}
