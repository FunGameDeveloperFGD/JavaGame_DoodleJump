import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener {

    final int WIDTH = 400;
    final int HEIGHT = 533;

    Thread thread;
    boolean isRunning;
    Texture t1, t2, t3;
    Sprite sBackground, sPlatform, sPlayer;
    Point[] pPlatform;

    BufferedImage gameView;

    int x = 100, y = 100, h = 150;
    float dx = 0, dy = 0;
    boolean right, left;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);

        gameView = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }

    public void start() {
        t1 = new Texture("/images/background.png");
        t2 = new Texture("/images/platform.png");
        t3 = new Texture("/images/doodle.png");

        sBackground = new Sprite(t1);
        sPlatform = new Sprite(t2);
        sPlayer = new Sprite(t3);

        pPlatform = new Point[20];

        for (int i = 0; i < 10; i++) {
            pPlatform[i] = new Point();
            pPlatform[i].x = new Random().nextInt(400) % 400;
            pPlatform[i].y = new Random().nextInt(533) % 533;
        }
    }

    public void update() {
        if (right) {
            x += 3;
        } else if (left) {
            x -= 3;
        }

        if (x + sPlayer.getTexture().getWidth() > WIDTH) {
            x = WIDTH - sPlayer.getTexture().getWidth();
        } else if (x < 0) {
            x = 0;
        }

        dy += 0.2;
        y += dy;
        if (y > 500) {
            dy = -10;
        }

        if (y < h) {
            for (int i = 0; i < 10; i++) {
                y = h;
                pPlatform[i].y = pPlatform[i].y - (int) dy;
                if (pPlatform[i].y > 533) {
                    pPlatform[i].y = 0;
                    pPlatform[i].x = new Random().nextInt(400) % 400;
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            if ((x + 50 > pPlatform[i].x) && (x + 20 < pPlatform[i].x + 68) && (y + 70 > pPlatform[i].y) && (y + 70 < pPlatform[i].y + 14) && (dy > 0)) {
                dy = -10;
            }
        }
    }

    public void draw() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = gameView.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //Draw
        {
            g.drawImage(sBackground.getTexture().getImage(), 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(sPlayer.getTexture().getImage(), x, y, sPlayer.getTexture().getImage().getWidth(), sPlayer.getTexture().getImage().getHeight(), null);

            for (int i = 0; i < 10; i++) {
                g.drawImage(sPlatform.getTexture().getImage(), pPlatform[i].x, pPlatform[i].y, sPlatform.getTexture().getWidth(), sPlatform.getTexture().getHeight(), null);
            }

        }
        //End draw
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(gameView, 0, 0, WIDTH, HEIGHT, null);
        bs.show();
    }

    @Override
    public void run() {
        try {
            start();
            long lastTime = System.nanoTime();
            double fps = 60.0;
            double ns = 1000000000 / fps;
            double delta = 0;
            while (isRunning) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1) {
                    update();
                    draw();
                    delta--;
                }
            }
        } catch (Exception exception) {
            isRunning = false;
            thread.interrupt();
            exception.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
    }
}
