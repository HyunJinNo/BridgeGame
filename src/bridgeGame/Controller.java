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
    private int count;
    private int turn;

    public Controller(TitleView titleView, GameView gameView) {
        this.titleView = titleView;
        this.gameView = gameView;
        turn = 0;

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

            // PLAY
            titleView.buttons[0].addActionListener(e -> {
                String num = JOptionPane.showInputDialog(new JFrame(), "플레이어의 수를 입력하세요");
                if (num == null) {
                    JOptionPane.showMessageDialog(new JFrame(), "값이 입력되지 않았습니다");
                } else if (num.equals("2") || num.equals("3") || num.equals("4")) {
                    players = new Player[Integer.parseInt(num)];
                    for (int i = 0; i < players.length; i++) {
                        JLabel image = new JLabel();
                        image.setIcon(new ImageIcon("src/resources/p" + (i + 1) + ".png"));
                        players[i] = new Player(image);
                        count = players.length - 1;
                    }
                    cardLayout.show(panel, "gameView");
                    gameView.init(players, filename, this);
                    gameView.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "2, 3, 4중 하나의 값을 입력해주세요");
                }
            });

            // LOAD
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

            // ROLL
            gameView.buttons[0].addActionListener(e-> {
                for (JLabel image : gameView.dice.getImages()) {
                    image.setVisible(false);
                }

                int num = gameView.dice.rollDice() - players[turn].getCard().getNum();
                if (num > 0) {
                    Outter: while (true) {
                        String input = JOptionPane.showInputDialog(new JFrame(),
                                "U, D, L, R 또는 u, d, l, r의 조합을 한줄로 입력해주세요.\n" +
                                num + "번 이동하실 수 있습니다.");
                        if (input == null) {
                            continue;
                        }

                        if (input.length() == num) {
                            switch (input.charAt(0)) {
                                case 'U': case 'D': case 'L': case 'R':
                                    for (int i = 1; i < input.length(); i++) {
                                        switch (input.charAt(i)) {
                                            case 'U': case 'D': case 'L': case 'R':
                                                break;
                                            default:
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "잘못된 문자를 입력하셨습니다.");
                                                continue Outter;
                                        }
                                    }
                                    break;
                                case 'u': case 'd': case 'l': case 'r':
                                    for (int i = 1; i < input.length(); i++) {
                                        switch (input.charAt(i)) {
                                            case 'u': case 'd': case 'l': case 'r':
                                                break;
                                            default:
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "잘못된 문자를 입력하셨습니다.");
                                                continue Outter;
                                        }
                                    }
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(new JFrame(),
                                            "잘못된 문자를 입력하셨습니다.");
                                    continue;
                            }

                            int x = players[turn].getX();
                            int y = players[turn].getY();

                            for (int i = 0; i < input.length(); i++) {
                                switch (input.charAt(i)) {
                                    case 'U': case 'u':
                                        if (y >= 68 && gameView.map[(y - 68) / 68][x / 68] != null) {
                                            y -= 68;
                                        } else {
                                            JOptionPane.showMessageDialog(new JFrame(),
                                                    "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                            continue Outter;
                                        }
                                        break;
                                    case 'D': case 'd':
                                        if (y <= 832 && gameView.map[(y + 68) / 68][x / 68] != null) {
                                            y += 68;
                                        } else {
                                            JOptionPane.showMessageDialog(new JFrame(),
                                                    "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                            continue Outter;
                                        }
                                        break;
                                    case 'L': case 'l':
                                        if (x >= 68 && gameView.map[y / 68][(x - 68) / 68] != null) {
                                            x -= 68;
                                        } else {
                                            JOptionPane.showMessageDialog(new JFrame(),
                                                    "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                            continue Outter;
                                        }
                                        break;
                                    case 'R': case 'r':
                                        if (x <= 1432) {
                                            x += 68;
                                        } else {
                                            JOptionPane.showMessageDialog(new JFrame(),
                                                    "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                            continue Outter;
                                        }
                                        break;
                                }

                                if (gameView.map[y / 68][x / 68].getType().equals("End")) {
                                    break;
                                }
                            }

                            for (int i = 0; i < input.length(); i++) {
                                switch (input.charAt(i)) {
                                    case 'U': case 'u':
                                        players[turn].setY(players[turn].getY() - 68);
                                        break;
                                    case 'D': case 'd':
                                        players[turn].setY(players[turn].getY() + 68);
                                        break;
                                    case 'L': case 'l':
                                        players[turn].setX(players[turn].getX() - 68);
                                        break;
                                    case 'R': case 'r':
                                        players[turn].setX(players[turn].getX() + 68);
                                        break;
                                }

                                switch (gameView.map[players[turn].getY() / 68][players[turn].getX() / 68].getType()) {
                                    case "Saw":
                                        players[turn].plusScore(3);
                                        break;
                                    case "Bridge":
                                        players[turn].getCard().getOneCard();
                                        break;
                                    case "Hammer":
                                        players[turn].plusScore(2);
                                        break;
                                    case "Philips Driver":
                                        players[turn].plusScore(1);
                                        break;
                                    case "End":
                                        players[turn].finish();
                                        switch (players.length - count) {
                                            case 1:
                                                players[turn].plusScore(7);
                                                break;
                                            case 2:
                                                players[turn].plusScore(3);
                                                break;
                                            case 3:
                                                players[turn].plusScore(1);
                                                break;
                                        }
                                        count--;
                                        break Outter;
                                    default:
                                        break;
                                }
                            }
                            break;
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(),
                                    "이동 횟수와 일치하지 않습니다.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "이동하실 수 있는 횟수가 0번입니다.");
                }

                // Turn 을 다음 Player 에게 넘김.
                do {
                    turn = (turn + 1) % players.length;
                } while (!players[turn].isActive());

                for (int i = 0; i < players.length; i++) {
                    if (players[i].isActive()) {
                        gameView.nameLabels[i].setForeground(Color.BLACK);
                    } else {
                        gameView.nameLabels[i].setForeground(Color.GRAY);
                    }
                }
                gameView.nameLabels[turn].setForeground(Color.CYAN);

                gameView.buttons[0].setEnabled(true);
                gameView.buttons[1].setEnabled(players[turn].getCard().getNum() != 0);
            });

            // STAY
            gameView.buttons[1].addActionListener(e-> {
                for (JLabel image : gameView.dice.getImages()) {
                    image.setVisible(false);
                }

                // 카드 1장을 반납함.
                players[turn].getCard().returnOneCard();

                // Turn 을 다음 Player 에게 넘김.
                do {
                    turn = (turn + 1) % players.length;
                } while (!players[turn].isActive());

                for (int i = 0; i < players.length; i++) {
                    if (players[i].isActive()) {
                        gameView.nameLabels[i].setForeground(Color.BLACK);
                    } else {
                        gameView.nameLabels[i].setForeground(Color.GRAY);
                    }
                }
                gameView.nameLabels[turn].setForeground(Color.CYAN);

                gameView.buttons[0].setEnabled(true);
                gameView.buttons[1].setEnabled(players[turn].getCard().getNum() != 0);
            });

            frame.setVisible(true);
        });
    }

    public int getCount() {
        return count;
    }
}
