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
    private String filename = "src/bridgeGame/another.map";  // 수정할 것.
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
                            // U, D, L, R 또는 u, d, l, r의 조합을 입력했는지 확인.
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

                            if (count <= players.length - 2) {
                                // 어떤 플레이어가  END 를 넘었을 경우 보드에 있는
                                // 나머지 플레이어들은 더 이상 뒤로 이동 불가능.
                                for (int i = 0; i < input.length(); i++) {
                                    switch (gameView.map[y / 68][x / 68].getType()) {
                                        case "Start":
                                            if (!input.substring(i, i + 1).equalsIgnoreCase(gameView.map[y / 68][x / 68].getDirections()[0])) {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outter;
                                            }
                                            break;
                                        case "Saw":
                                        case "Cell":
                                        case "Hammer":
                                        case "Philips Driver":
                                        case "Bridge":
                                            if (!input.substring(i, i + 1).equalsIgnoreCase(gameView.map[y / 68][x / 68].getDirections()[1])) {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outter;
                                            }
                                            break;
                                        case "Bridge Cell":
                                            if (!input.substring(i, i + 1).equalsIgnoreCase(gameView.map[y / 68][x / 68].getDirections()[1])
                                                    && !input.substring(i, i + 1).equalsIgnoreCase("R")) {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outter;
                                            }
                                            break;
                                        default:
                                            break;
                                    }

                                    switch (input.charAt(i)) {
                                        case 'U': case 'u':
                                            y -= 68;
                                            break;
                                        case 'D': case 'd':
                                            y += 68;
                                            break;
                                        case 'L': case 'l':
                                            x -= 68;
                                            break;
                                        case 'R': case 'r':
                                            x += 68;
                                            break;
                                    }

                                    if (gameView.map[y / 68][x / 68].getType().equals("End")) {
                                        break;
                                    }
                                }
                            } else {
                                // 아직 아무도 END 를 넘지 않았을 경우.
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
                                            if (x <= 1432 && gameView.map[y / 68][(x + 68) / 68] != null) {
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
                            }

                            // Move
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

                                // Player 가 도구 카드를 집거나 End 에 도달했을 때.
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

                // 게임이 종료되었을 때 각 플레이어의 점수 표시.
                if (count == 0) {
                    for (JButton button : gameView.buttons) {
                        button.setEnabled(false);
                    }

                    StringBuilder sb = new StringBuilder(1000000);
                    sb.append("Scores: \n");
                    for (int i = 0; i < players.length; i++) {
                        sb.append("Player").append(i + 1).append(": ").append(players[i].getScore()).append("\n");
                    }

                    JOptionPane.showMessageDialog(new JFrame(), sb.toString());
                }
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
