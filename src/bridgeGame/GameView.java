package bridgeGame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public final class GameView extends JPanel implements ActionListener {
    // GameView 의 Width
    private static final int WIDTH = 1500;

    // GameView 의 Height
    private static final int HEIGHT = 900;

    // gameView 의 2개의 버튼: Roll, Stay
    final JButton[] buttons;

    // 플레이어 정보
    private Player[] players;

    // Cell 목록
    private final ArrayList<Cell> cells;

    // 지도 데이터에 해당하는 변수로 값이 null 이면 해당 위치에 Cell 이 없음을 의미함.
    Cell[][] map;

    // 주사위
    final Dice dice;

    // 게임 화면 상에 각 플레이어가 가진 카드의 수를 구분하기 위해 사용됨.
    // 또한 현재 어떤 플레이어의 턴인지를 나타내는데 사용됨.
    JLabel[] nameLabels;

    public GameView() {
        cells = new ArrayList<>();
        dice = new Dice();

        // 버튼 텍스트, 폰트 크기, 버튼 위치 등 설정.
        Font font = new Font("Default", Font.BOLD, 17);
        buttons = new JButton[2];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setContentAreaFilled(false);
            buttons[i].setBorder(new BevelBorder(BevelBorder.RAISED));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(font);
            add(buttons[i]);
        }
        buttons[0].setText("Roll");
        buttons[1].setText("Stay");
        buttons[0].setBounds(1350, 25, 100, 50);
        buttons[1].setBounds(1350, 100, 100, 50);
        buttons[1].setEnabled(false);

        setLayout(null);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    // 게임 시작 시 플레이어 위치, 지도 데이터 로드, 카드 개수 표시 등의 역할을 수행하는 메소드
    public void init(Player[] players, String filename) {
        this.players = players;

        // 지도 데이터 파일을 로드함. 파일이 존재하지 않을 경우 프로그램 종료함.
        File file = new File(filename);
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "지도 데이터 파일을 찾을 수 없습니다.");
            System.exit(0);
            return;
        }

        // Cell 의 위치를 정하기 위해 사용하는 변수
        int x = 0;
        int y = 0;
        
        // 지도 데이터 파일을 로드했을 때 그 데이터를 2차원 배열에 옮기기 위해
        // 사용하는 변수
        int maxX = -1;
        int maxY = -1;

        while (true) {
            try {
                String[] data = br.readLine().split(" ");
                JLabel label = new JLabel();
                Cell cell;

                // Cell 종류에 따라 이미지 파일, Cell 의  type 등을 결정한다.
                switch (data[0]) {
                    case "S":
                        if (data.length == 2) {
                            // Start
                            label.setIcon(new ImageIcon("src/resources/start.png"));
                            cell = new Cell(x, y, "Start", new String[] { data[1] }, label);
                        } else {
                            // Saw
                            label.setIcon(new ImageIcon("src/resources/saw.png"));
                            cell = new Cell(x, y, "Saw", new String[] { data[1], data[2] }, label);
                        }
                        break;
                    case "C":
                    case "b":
                        // Cell
                        label.setIcon(new ImageIcon("src/resources/cell.png"));
                        cell = new Cell(x, y, "Cell", new String[] { data[1], data[2] }, label);
                        break;
                    case "B":
                        // Bridge_cell
                        label.setIcon(new ImageIcon("src/resources/bridge_cell.png"));
                        cell = new Cell(x, y, "Bridge Cell", new String[] { data[1], data[2] }, label);
                        cells.add(cell);
                        label = new JLabel();
                        label.setIcon(new ImageIcon("src/resources/bridge.png"));
                        cell = new Cell(x + 68, y, "Bridge", new String[] { "L", "R" }, label);
                        break;
                    case "H":
                        // Hammer
                        label.setIcon(new ImageIcon("src/resources/hammer.png"));
                        cell = new Cell(x, y, "Hammer", new String[] { data[1], data[2] }, label);
                        break;
                    case "P":
                        // Philips Driver
                        label.setIcon(new ImageIcon("src/resources/driver.png"));
                        cell = new Cell(x, y, "Philips Driver", new String[] { data[1], data[2] }, label);
                        break;
                    case "E":
                        // End
                        label.setIcon(new ImageIcon("src/resources/end.png"));
                        cell = new Cell(x, y, "End", new String[] {}, label);
                        break;
                    default:
                        JOptionPane.showMessageDialog(new JFrame(), "해당 지도 데이터에 오류가 존재합니다.");
                        System.exit(0);
                        return;
                }
                cells.add(cell);

                // End 을 제외한 모든 Cell 에 대해 다음 방향에 해당하는 위치로 x, y 값을 변경함.
                // 또한 다음 방향에 해당하는 위치가 화면 왼쪽 또는 위쪽일 경우 모든 Cell 의 위치를 조정하는 역할도 수행함.
                // 단, 다음 뱡향에 해당하는 위치가 화면 아래 또는 오른쪽일 경우,
                // 즉, 지도의 크기가 게임 화면에 표시하기에 너무 클 경우
                // 게임이 진행되지 않고 프로그램을 죵료시킴.
                if (data.length >= 2) {
                    switch (data[data.length - 1]) {
                        case "U":
                            if (y < 68) {
                                for (Cell value : cells) {
                                    value.setY(value.getY() + 68);
                                }
                            } else {
                                y -= 68;
                            }
                            break;
                        case "D":
                            if (y > 832) {
                                JOptionPane.showMessageDialog(new JFrame(), "지도의 세로 크기가 너무 큽니다.");
                                System.exit(0);
                            } else {
                                y += 68;
                            }
                            break;
                        case "L":
                            if (x < 68) {
                                for (Cell value : cells) {
                                    value.setX(value.getX() + 68);
                                }
                            } else {
                                x -= 68;
                            }
                            break;
                        case "R":
                            if (x > 1432) {
                                JOptionPane.showMessageDialog(new JFrame(), "지도의 가로 크기가 너무 큽니다.");
                                System.exit(0);
                            } else {
                                x += 68;
                            }
                            break;
                        default:
                            JOptionPane.showMessageDialog(new JFrame(), "해당 지도 데이터에 오류가 존재합니다.");
                            System.exit(0);
                            return;
                    }

                    if (x > maxX)
                        maxX = x;
                    if (y > maxY)
                        maxY = y;
                }
            } catch (Exception e) {
                break;
            }
        }

        // 지도 데이터 값 초기화.
        map = new Cell[(maxY / 68) + 1][(maxX / 68) + 1];
        for (int i = 0; i < map.length; i++) {
            map[i] = new Cell[(maxY / 68) + 1];
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = null;
            }
        }

        // 2차원 배열에 지도 데이터 입력.
        for (Cell value : cells) {
            map[value.getY() / 68][value.getX() / 68] = value;
        }

        // 각 플레이어의 시작 위치
        int[][] pos = {
                { cells.get(0).getX() + 2, cells.get(0).getY() + 2 },
                { cells.get(0).getX() + 36, cells.get(0).getY() + 2 },
                { cells.get(0).getX() + 2, cells.get(0).getY() + 36 },
                { cells.get(0).getX() + 36, cells.get(0).getY() + 36 }
        };

        // 주사위 이미지를 화면에 추가. 주사위 이미지는 주사위를 굴리기 전에는 보이지 않음.
        for (int i = 0; i < dice.getImages().length; i++) {
            add(dice.getImages()[i]);
        }

        // 각 PLAYER 를 나타내는 JLabel
        Font font = new Font("Default", Font.BOLD, 17);
        nameLabels = new JLabel[players.length];
        for (int i = 0; i < nameLabels.length; i++) {
            nameLabels[i] = new JLabel("PLAYER" + (i + 1) + ":");
            nameLabels[i].setBounds(1320, (i * 60) + 300, 100, 50);
            nameLabels[i].setFont(font);
            add(nameLabels[i]);
        }
        nameLabels[0].setForeground(Color.CYAN);

        // 카드의 수를 나타냄.
        for (int i = 0; i < players.length; i++) {
            JLabel numLabel = players[i].getCard().getNumLabel();
            numLabel.setBounds(1430, (i * 60) + 300, 50, 50);
            add(numLabel);
        }

        // 카드 이미지를 나타냄.
        JLabel[] cards = new JLabel[players.length];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new JLabel(new ImageIcon("src/resources/card.png"));
            cards[i].setBounds(1420, (i * 60) + 300, 50, 50);
            add(cards[i]);
        }

        // 각 Player 의 위치를 설정함.
        for (int i = 0; i < players.length; i++) {
            players[i].setX(pos[i][0]);
            players[i].setY(pos[i][1]);
            JLabel image = players[i].getImage();
            image.setBounds(players[i].getX(), players[i].getY(), 30, 30);
            add(image);
        }

        // Cell 을 화면에 표시.
        for (Cell cell : cells) {
            JLabel image = cell.getImage();
            image.setBounds(cell.getX(), cell.getY(), 68, 68);
            add(image);
        }

        // background 이미지 출력.
        JLabel background = new JLabel(new ImageIcon("src/resources/background.png"));
        background.setBounds(0, 0, 1500, 900);
        add(background);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 각 플레이어의 위치와 카드의 수를 표시함.
        for (Player player : players) {
            JLabel image = player.getImage();
            image.setBounds(player.getX(), player.getY(), 30, 30);

            JLabel numLabel = player.getCard().getNumLabel();
            numLabel.setText(String.valueOf(player.getCard().getNum()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
