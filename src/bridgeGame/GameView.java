package bridgeGame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public final class GameView extends JPanel implements ActionListener {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 900;
    private Player[] players;
    private final ArrayList<Cell> cells;
    protected Cell[][] map;
    private Controller controller;
    private Timer timer;
    protected final JButton[] buttons;
    protected final Dice dice;
    protected JLabel[] nameLabels;

    public GameView() {
        cells = new ArrayList<Cell>();
        dice = new Dice();

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

    public void init(Player[] players, String filename, Controller controller) {
        this.players = players;
        this.controller = controller;

        File file = new File(filename);
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // TODO: return to TitleView
            return;
        }

        int x = 0;
        int y = 0;
        int maxX = -1;
        int maxY = -1;

        while (true) {
            try {
                String[] data = br.readLine().split(" ");
                JLabel label = new JLabel();
                Cell cell;

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
                        cell = new Cell(x, y, "Cell", new String[] { data[1], data[2] }, label);
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
                        // TODO: Wrong Input
                        return;
                }
                cells.add(cell);

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
                                for (Cell value : cells) {
                                    value.setY(value.getY() - 68);
                                }
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
                                for (Cell value : cells) {
                                    value.setX(value.getX() - 68);
                                }
                            } else {
                                x += 68;
                            }
                            break;
                        default:
                            // TODO: Wrong Input
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

        map = new Cell[(maxY / 68) + 1][(maxX / 68) + 1];
        for (int i = 0; i < map.length; i++) {
            map[i] = new Cell[(maxY / 68) + 1];
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = null;
            }
        }

        for (Cell value : cells) {
            map[value.getY() / 68][value.getX() / 68] = value;
        }

        int[][] pos = {
                { cells.get(0).getX() + 2, cells.get(0).getY() + 2 },
                { cells.get(0).getX() + 36, cells.get(0).getY() + 2 },
                { cells.get(0).getX() + 2, cells.get(0).getY() + 36 },
                { cells.get(0).getX() + 36, cells.get(0).getY() + 36 }
        };

        // 주사위 이미지를 출력.
        for (int i = 0; i < dice.getImages().length; i++) {
            add(dice.getImages()[i]);
        }

        // PLAYER 이름을 나타내는 JLabel
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

        // 각 Player 의 위치를 나타냄.
        for (int i = 0; i < players.length; i++) {
            players[i].setX(pos[i][0]);
            players[i].setY(pos[i][1]);
            JLabel image = players[i].getImage();
            image.setBounds(players[i].getX(), players[i].getY(), 30, 30);
            add(image);
        }

        // Cell 을 표시.
        for (Cell cell : cells) {
            JLabel image = cell.getImage();
            image.setBounds(cell.getX(), cell.getY(), 68, 68);
            add(image);
        }

        // background 이미지 출력.
        JLabel background = new JLabel(new ImageIcon("src/resources/background.png"));
        background.setBounds(0, 0, 1500, 900);
        add(background);

        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controller.getCount() > 0) {
            for (Player player : players) {
                JLabel image = player.getImage();
                image.setBounds(player.getX(), player.getY(), 30, 30);

                JLabel numLabel = player.getCard().getNumLabel();
                numLabel.setText(String.valueOf(player.getCard().getNum()));
            }
        } else {
            timer.stop();
            // TODO: GAME OVER
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
