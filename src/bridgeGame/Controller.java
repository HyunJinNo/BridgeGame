package bridgeGame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public final class Controller {
    private JFrame frame;
    private JPanel panel;
    private CardLayout cardLayout;

    // 플레이어 정보를 나타낸다. 게임이 시작될 때 입력받은 플레이어의 수만큼 Player 인스턴스가 생성된다.
    private Player[] players;

    // 프로그램이 시작될 때 디폴트로 로드하는 지도 데이터 파일을 초기값으로 갖으며
    // LOAD 기능을 통해 다른 지도 데이터 파일을 로드할 수 있다.
    private String filename = "src/bridgeGame/default.map";

    // 게임이 종료되기까지 남은 플레이어의 수를 나타내며
    // 이 값이 0이 되면 게임이 종료된다.
    private int count;

    // 플레이어의 턴을 나타내는 변수로 players 배열의 인덱스에 해당한다.
    private int turn;

    public Controller(TitleView titleView, GameView gameView) {
        // 게임 시작 시 Player1 의 순서로 시작되도록 값을 0으로 초기화함.
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

            // TitleView 의 PLAY 버튼 기능 구현.
            titleView.buttons[0].addActionListener(e -> {
                // 플레이어의 수를 입력받음.
                String num = JOptionPane.showInputDialog(new JFrame(), "플레이어의 수를 입력하세요");

                if (num == null) {
                    // 값이 입력되지 않았을 경우의 메세지 출력.
                    JOptionPane.showMessageDialog(new JFrame(), "값이 입력되지 않았습니다");
                } else if (num.equals("2") || num.equals("3") || num.equals("4")) {
                    // 2, 3, 4 중 하나의 값을 입력했을 때 게임 진행.

                    // 입력받은 수만큼 플레이어 생성
                    players = new Player[Integer.parseInt(num)];

                    // 입력받은 수만큼 플레이어를 Start 지점에 위치시킴.
                    // 또한 게임이 종료되기까지의 플레이어의 수를 count 값으로 초기화함.
                    for (int i = 0; i < players.length; i++) {
                        JLabel image = new JLabel();
                        image.setIcon(new ImageIcon("src/resources/p" + (i + 1) + ".png"));
                        players[i] = new Player(image);
                        count = players.length - 1;
                    }

                    // TitleView 에서 GameView 로 전환.
                    cardLayout.show(panel, "gameView");
                    gameView.init(players, filename);
                    gameView.requestFocusInWindow();
                } else {
                    // 잘못된 값을 입력했을 때의 메세지 출력.
                    JOptionPane.showMessageDialog(new JFrame(), "2, 3, 4중 하나의 값을 입력해주세요");
                }
            });

            // TitleView 의 LOAD 버튼 기능 구현.
            titleView.buttons[1].addActionListener(e -> {
                // 확장자가 map 인 파일만 로드할 수 있도록 함.
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Select an map file");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("map", "map");
                jfc.addChoosableFileFilter(filter);

                // 파일을 로드했으면 해당 파일로 로드할 준비.
                // 파일 로드를 취소했으면 로드할 파일을 변경하지 않음.
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    filename = jfc.getSelectedFile().getAbsolutePath();
                    String[] temp = filename.split("\\\\");
                    String path = temp[temp.length - 1];
                    titleView.filenameLabel.setText("File: " + path);

                }
            });

            // GameView 의 ROLL 버튼 기능 구현.
            gameView.buttons[0].addActionListener(e-> {
                // 주사위의 값과 플레이어가 가진 카드의 수에 따라 이동 횟수를 결정함.
                int num = gameView.dice.rollDice() - players[turn].getCard().getNum();

                // 이동 횟수가 1 이상일 경우.
                if (num > 0) {
                    Outer: while (true) {
                        // U, D, L, R 또는 u, d, l, r 의 조합을 한 줄로 입력받음.
                        // 이 때 이동 횟수만큼 입력받는다.
                        String input = JOptionPane.showInputDialog(new JFrame(),
                                "U, D, L, R 또는 u, d, l, r의 조합을 한줄로 입력해주세요.\n" +
                                num + "번 이동하실 수 있습니다.");

                        // 아무 것도 입력받지 않았을 경우 다시 입력받음.
                        if (input == null) {
                            continue;
                        }

                        // 이동 횟수만큼 문자열을 입력받았을 경우.
                        if (input.length() == num) {
                            // U, D, L, R 또는 u, d, l, r의 조합을 입력했는지 확인.
                            // 만약 대문자와 소문자가 섞여있거나, 또는 아예 다른 문자를 입력받은 경우
                            // 다시 입력받는다.
                            switch (input.charAt(0)) {
                                case 'U': case 'D': case 'L': case 'R':
                                    for (int i = 1; i < input.length(); i++) {
                                        switch (input.charAt(i)) {
                                            case 'U': case 'D': case 'L': case 'R':
                                                break;
                                            default:
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "잘못된 문자를 입력하셨습니다.");
                                                continue Outer;
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
                                                continue Outer;
                                        }
                                    }
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(new JFrame(),
                                            "잘못된 문자를 입력하셨습니다.");
                                    continue;
                            }

                            // 플레이어의 현재 위치를 확인함.
                            int x = players[turn].getX();
                            int y = players[turn].getY();

                            if (count <= players.length - 2) {
                                // 어떤 플레이어가 END 를 넘었을 경우 보드에 있는
                                // 나머지 플레이어들은 더 이상 뒤로 이동 불가능.
                                // 플레이어의 위치로부터 다음 위치로 이동할 수 있는지 확인.
                                // 현재 위치로부터 입력받은 방향에 Cell 이 존재하지 않으면 이동 불가.
                                // 만약 뒤로 이동하는 문자를 입력받았을 경우
                                // 다시 입력받는다.
                                for (int i = 0; i < input.length(); i++) {
                                    switch (gameView.map[y / 68][x / 68].getType()) {
                                        case "Start":
                                            if (!input.substring(i, i + 1).equalsIgnoreCase(gameView.map[y / 68][x / 68].getDirections()[0])) {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outer;
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
                                                continue Outer;
                                            }
                                            break;
                                        case "Bridge Cell":
                                            if (!input.substring(i, i + 1).equalsIgnoreCase(gameView.map[y / 68][x / 68].getDirections()[1])
                                                    && !input.substring(i, i + 1).equalsIgnoreCase("R")) {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outer;
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

                                    // 플레이어가 End 에 도달하도록 입력받은 경우 검사 종료.
                                    if (gameView.map[y / 68][x / 68].getType().equals("End")) {
                                        break;
                                    }
                                }
                            } else {
                                // 아직 아무도 END 를 넘지 않았을 경우 보드에 있는 모든
                                // 플레이어는 뒤로 이동 가능.
                                // 플레이어의 위치로부터 다음 위치로 이동할 수 있는지 확인.
                                // 현재 위치로부터 입력받은 방향에 Cell 이 존재하지 않으면 이동 불가.
                                for (int i = 0; i < input.length(); i++) {
                                    switch (input.charAt(i)) {
                                        case 'U': case 'u':
                                            if (y >= 68 && gameView.map[(y - 68) / 68][x / 68] != null) {
                                                y -= 68;
                                            } else {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outer;
                                            }
                                            break;
                                        case 'D': case 'd':
                                            if (y <= 832 && gameView.map[(y + 68) / 68][x / 68] != null) {
                                                y += 68;
                                            } else {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outer;
                                            }
                                            break;
                                        case 'L': case 'l':
                                            if (x >= 68 && gameView.map[y / 68][(x - 68) / 68] != null) {
                                                x -= 68;
                                            } else {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outer;
                                            }
                                            break;
                                        case 'R': case 'r':
                                            if (x <= 1432 && gameView.map[y / 68][(x + 68) / 68] != null) {
                                                x += 68;
                                            } else {
                                                JOptionPane.showMessageDialog(new JFrame(),
                                                        "이동할 수 없는 방향 정보가 포함되어 있습니다.");
                                                continue Outer;
                                            }
                                            break;
                                    }

                                    // 플레이어가 End 에 도달하도록 입력받은 경우 검사 종료.
                                    if (gameView.map[y / 68][x / 68].getType().equals("End")) {
                                        break;
                                    }
                                }
                            }

                            // 입력받은 방향 문자열의 유효하므로 플레이어를 이동시킨다.
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

                                // Player 가 도구 카드를 집거나 End 에 도달했을 때 플레이어의 점수 추가.
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
                                        // End 에 도달한 경우 플레이어의 게임이 종료됨.
                                        // 또한 End 에 도달한 순서에 따라 추가되는 점수를 결정함.
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

                                        // End 에 도달하였으므로 게임이 종료되기까지 남아있는
                                        // 플레이어의 수를 나타내는 count 값을 1 감소시킴.
                                        count--;
                                        break Outer;
                                    default:
                                        break;
                                }
                            }
                            break;
                        } else {
                            // 이동 횟수와 다른 길이의 문자열을 입력받았을 경우 출력하는 메세지.
                            // 다시 입력받는다.
                            JOptionPane.showMessageDialog(new JFrame(),
                                    "이동 횟수와 일치하지 않습니다.");
                        }
                    }
                } else {
                    // 이동 횟수가 없는 경우 출력하는 메세지.
                    // 다시 입력받지 않고 다음 플레이어에게 턴을 넘긴다.
                    JOptionPane.showMessageDialog(new JFrame(),
                            "이동하실 수 있는 횟수가 0번입니다.");
                }

                // Turn 을 다음 Player 에게 넘김.
                // 이 때 이미 End 에 도달한 플레이어에게 Turn 을 넘기지 않는다.
                do {
                    turn = (turn + 1) % players.length;
                } while (!players[turn].isActive());

                // 어떤 플레이어의 턴인지를 나타냄.
                // 또한 게임이 종료된 플레이어를 다른 플레이어와 구분함.
                for (int i = 0; i < players.length; i++) {
                    if (players[i].isActive()) {
                        gameView.nameLabels[i].setForeground(Color.BLACK);
                    } else {
                        gameView.nameLabels[i].setForeground(Color.GRAY);
                    }
                }
                gameView.nameLabels[turn].setForeground(Color.CYAN);

                // 카드의 수가 0장이면 Stay 버튼이 동작하지 않게 함.
                // 카드의 수가 1장 이상이면 Stay 버튼이 동작하게 함.
                gameView.buttons[0].setEnabled(true);
                gameView.buttons[1].setEnabled(players[turn].getCard().getNum() != 0);

                // 게임이 종료되었을 때 각 플레이어의 점수를 표시한 후 프로그램 종료.
                if (count == 0) {
                    // Roll 버튼과 Stay 버튼이 더 이상 동작하지 않게 함.
                    for (JButton button : gameView.buttons) {
                        button.setEnabled(false);
                    }

                    // 각 플레이어의 점수를 표시한 후 프로그램 종료.
                    StringBuilder sb = new StringBuilder(1000000);
                    sb.append("Scores: \n");
                    for (int i = 0; i < players.length; i++) {
                        sb.append("Player").append(i + 1).append(": ").append(players[i].getScore()).append("\n");
                    }
                    JOptionPane.showMessageDialog(new JFrame(), sb.toString());
                    System.exit(0);
                }

                for (JLabel image : gameView.dice.getImages()) {
                    image.setVisible(false);
                }
            });

            // gameView 의 Stay 버튼 기능 구현.
            gameView.buttons[1].addActionListener(e-> {
                // 카드 1장을 반납함.
                players[turn].getCard().returnOneCard();

                // Turn 을 다음 Player 에게 넘김.
                do {
                    turn = (turn + 1) % players.length;
                } while (!players[turn].isActive());

                // 어떤 플레이어의 턴인지를 나타냄.
                // 또한 게임이 종료된 플레이어를 다른 플레이어와 구분함.
                for (int i = 0; i < players.length; i++) {
                    if (players[i].isActive()) {
                        gameView.nameLabels[i].setForeground(Color.BLACK);
                    } else {
                        gameView.nameLabels[i].setForeground(Color.GRAY);
                    }
                }
                gameView.nameLabels[turn].setForeground(Color.CYAN);

                // 카드의 수가 0장이면 Stay 버튼이 동작하지 않게 함.
                // 카드의 수가 1장 이상이면 Stay 버튼이 동작하게 함.
                gameView.buttons[0].setEnabled(true);
                gameView.buttons[1].setEnabled(players[turn].getCard().getNum() != 0);

                for (JLabel image : gameView.dice.getImages()) {
                    image.setVisible(false);
                }
            });

            frame.setVisible(true);
        });
    }
}
