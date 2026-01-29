import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener {

    static final int TILE = 40;

    MapData map = new MapData();

    Thief thief = new Thief(1, 1);
    ArrayList<Police> policeList = new ArrayList<>();

    BFSPathFinder pathFinder;

    int time = 0;
    int score = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(
                map.cols * TILE,
                map.rows * TILE));

        policeList.add(new Police(5, 1));
        policeList.add(new Police(5, 7));

        pathFinder = new BFSPathFinder(map.map);

        addKeyListener(this);
        setFocusable(true);

        Timer gameTimer = new Timer(500, e -> updateGame());
        gameTimer.start();

        Timer timeTimer = new Timer(1000, e -> {
            time++;
            score += 5;
        });
        timeTimer.start();
    }

    void updateGame() {
        movePolice();
        repaint();

        for (Police p : policeList) {
            if (p.x == thief.x && p.y == thief.y) {
                JOptionPane.showMessageDialog(this,
                        "🚓 تم القبض على اللص!\nالنتيجة: " + score);
                System.exit(0);
            }
        }
    }

    void movePolice() {
        for (Police p : policeList) {
            List<Node> path = pathFinder.findPath(
                    p.x, p.y, thief.x, thief.y);

            if (path != null && path.size() > 1) {
                p.x = path.get(1).x;
                p.y = path.get(1).y;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < map.rows; r++) {
            for (int c = 0; c < map.cols; c++) {
                g.setColor(map.map[r][c] == '#' ? Color.DARK_GRAY : Color.WHITE);
                g.fillRect(c * TILE, r * TILE, TILE, TILE);
                g.setColor(Color.GRAY);
                g.drawRect(c * TILE, r * TILE, TILE, TILE);
            }
        }

        g.setColor(Color.RED);
        g.fillOval(thief.y * TILE, thief.x * TILE, TILE, TILE);

        g.setColor(Color.BLUE);
        for (Police p : policeList) {
            g.fillOval(p.y * TILE, p.x * TILE, TILE, TILE);
        }

        g.setColor(Color.BLACK);
        g.drawString("Time: " + time, 10, 15);
        g.drawString("Score: " + score, 100, 15);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int dx = 0, dy = 0;

        if (e.getKeyCode() == KeyEvent.VK_UP) dx = -1;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) dx = 1;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) dy = -1;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) dy = 1;

        int nx = thief.x + dx;
        int ny = thief.y + dy;

        if (map.isFree(nx, ny)) {
            thief.x = nx;
            thief.y = ny;
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
