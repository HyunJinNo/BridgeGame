package bridgeGame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public final class GameView extends JPanel {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 900;
    private Player[] players;
    private ArrayList<Cell> cells;

    public GameView() {
        cells = new ArrayList<Cell>();

        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void init(Player[] players, String filename) {
        this.players = players;
        int x = 0;
        int y = 0;

        File file = new File(filename);
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // TODO: return to TitleView
            return;
        }

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
                }
            } catch (Exception e) {
                break;
            }
        }

        int[][] pos = {
                { cells.get(0).getX() + 2, cells.get(0).getY() + 2 },
                { cells.get(0).getX() + 36, cells.get(0).getY() + 2 },
                { cells.get(0).getX() + 2, cells.get(0).getY() + 36 },
                { cells.get(0).getX() + 36, cells.get(0).getY() + 36 }
        };

        Font font = new Font("Default", Font.BOLD, 16);
        JLabel[] labels = new JLabel[players.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("P" + (i + 1) + ":");
            labels[i].setBounds(1350, (i * 60 + 10), 50, 50);
            labels[i].setFont(font);
            add(labels[i]);
        }

        JLabel[] cards = new JLabel[players.length];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new JLabel(new ImageIcon("src/resources/card.png"));
            cards[i].setBounds(1400, (i * 60) + 10, 50, 50);
            add(cards[i]);
        }

        for (int i = 0; i < players.length; i++) {
            players[i].setX(pos[i][0]);
            players[i].setY(pos[i][1]);
            JLabel image = players[i].getImage();
            image.setBounds(players[i].getX(), players[i].getY(), 30, 30);
            add(image);
        }

        for (Cell cell : cells) {
            JLabel image = cell.getImage();
            image.setBounds(cell.getX(), cell.getY(), 68, 68);
            add(image);
        }

        JLabel background = new JLabel(new ImageIcon("src/resources/background.png"));
        background.setBounds(0, 0, 1500, 900);
        add(background);
    }
}
