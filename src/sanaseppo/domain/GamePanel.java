/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sanaseppo.domain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Jaakko
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int PWIDTH = 1024;
    public static final int PHEIGHT = 768;
    
    private Thread animator;
    private boolean running = false;
    private boolean gameOver = false;
    
    private Graphics dbg;
    private Image dbImage = null;
    
    private int period = 1000 / 60;
    
    private LetterGrid lg;
    private int lastX, lastY;
    private DragTrail trail;
    
    private ArrayList<String> words;
    private ArrayList<String> foundWords;
    
    private int score;
    
    private boolean found = false;
    private long foundTime;
    private DragTrail foundTrail;
    
    private long startTime;
    private int timeLeft;
    
    private final int roundTime = 90;  //seconds

    public GamePanel() {
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        setFocusable(true);
        requestFocus();
        readyForTermination();

        WordParser parser = new TxtWordParser();
        this.words = parser.getWords("data/wordlist.txt");
        
        lg = new LetterGrid(4);
        lg.initialize(this.words);

        this.trail = new DragTrail();
        this.foundWords = new ArrayList<String>();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                //testPress(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                guessWord();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent me) {
                super.mouseDragged(me);
                testPress(me.getX(), me.getY());
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        startGame();
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleepTime;
        beforeTime = System.currentTimeMillis();

        running = true;
        
        this.startTime = System.currentTimeMillis();
        
        while (running) {
            gameUpdate();
            gameRender();

            paintScreen();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleepTime = period - timeDiff;

            if (sleepTime <= 0) {
                sleepTime = 5;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
            }

            beforeTime = System.currentTimeMillis();
        }

        System.exit(0);
    }

    private void startGame() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void stopGame() {
        running = false;
    }

    private void gameUpdate() {
        if (!gameOver) {
            if (this.found && System.currentTimeMillis() > (this.foundTime + 300)) {
                this.found = false;
                this.foundTrail = null;
            }
            
            this.timeLeft = (int) (this.roundTime - (System.currentTimeMillis() - this.startTime) / 1000);
        }
    }

    private void gameRender() {
        //Create buffer image
        if (dbImage == null) {
            dbImage = createImage(PWIDTH, PHEIGHT);

            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }

        //Clear background
        dbg.setColor(Color.BLACK);
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

        lg.draw(dbg);
        
        Color trailColor = Color.RED;
        if (found) {
            this.foundTrail.draw(dbg, Color.GREEN);
        }
        this.trail.draw(dbg, trailColor);
        
        
        dbg.drawString("Pisteet: " + this.score, 10, 500);
        
        int secs = this.timeLeft % 60;
        int mins = (this.timeLeft / 60);
        dbg.drawString(mins + ":" + String.format("%02d", secs), 10, 550);

        
        if (gameOver) {
            gameOverMessage(dbg);
        }

    }

    private void gameOverMessage(Graphics g) {
        g.drawString("Game over", 100, 100);
    }

    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if ((keyCode == KeyEvent.VK_ESCAPE)) {
                    running = false;
                }
            }
        });
    }

    private void testPress(int x, int y) {
        int gX = lg.getGridX(x);
        int gY = lg.getGridY(y);

        if ((gX != this.lastX || gY != this.lastY) && lg.contains(x, y)) {
            LetterCell currentCell =  lg.getCellAt(gX, gY);
            
            LetterCell prevCell = this.trail.getPreviousCell();
            int prevCellX = -1;
            int prevCellY = -1;
            
            if (prevCell != null) {
                prevCellX = lg.getGridX(prevCell.getX());
                prevCellY = lg.getGridY(prevCell.getY());
            }
            
            if (gX == prevCellX && gY == prevCellY) {
                this.trail.stepBack();
            } else if (currentCell != null) {
                this.trail.addCell(currentCell);
            }

            this.lastX = gX;
            this.lastY = gY;
        }
    }

    private void guessWord() {
        if (lg.wordFound(this.trail.getWord())
                && !this.foundWords.contains(this.trail.getWord())) {
            
            System.out.println(this.trail.getWord());
            this.foundWords.add(this.trail.getWord());
            
            int letters = this.trail.getWord().length();
            this.score += (Math.pow(letters, 2) - 2 * letters);
            
            this.found = true;
            this.foundTime = System.currentTimeMillis();
            this.foundTrail = this.trail;
        }
        
        this.trail = new DragTrail();
    }

    private void paintScreen() {
        Graphics g;

        try {
            g = this.getGraphics();

            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
            }

            g.dispose();
        } catch (Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    }
}
