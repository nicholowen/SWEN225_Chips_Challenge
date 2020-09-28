package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.MenuBar;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.ScorePanel;

/**
 * The Class GUI.
 */
public class GUI implements KeyListener {

    JFrame frame;
    GamePanel gamePanel;
    ScorePanel scorePanel;

    private boolean recording = false;
    private boolean paused = false;
    private String direction = null;
    JLayeredPane mainPanel;

    /**
     * Instantiates a new gui.
     */
    public GUI() {
        frame = new JFrame("Indecision Games: Chip's Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(800, 500));

        mainPanel = new JLayeredPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        frame.setContentPane(mainPanel);

        gamePanel = new GamePanel();
        scorePanel = new ScorePanel();
        mainPanel.add(gamePanel);
        mainPanel.add(scorePanel);

        MenuBar menu = new MenuBar();
        menu.setOpaque(true);
        frame.setJMenuBar(menu);

        JButton pausenplay = new JButton("pause");
        JButton save = new JButton("save");
        JButton load = new JButton("load");
        menu.add(pausenplay);

//        pausenplay.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ev) {
//                paused = !paused;
//                if (paused) {
//                    pausenplay.setText("Play");
//                } else
//                    pausenplay.setText("Pause");
//            }
//        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/icon.png").getImage());
        frame.pack();

        gamePanel.addKeyListener(this);

    }

    // =======================================================.
    // Key Listeners
    // =======================================================.

    /**
     * This method is called when a key is pressed on the keyboard.
     *
     * @param event which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            this.direction = "up";
            System.out.println(direction);
        } else if (keyCode == KeyEvent.VK_LEFT) {
            this.direction = "left";
            System.out.println(direction);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            this.direction = "down";
            System.out.println(direction);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            this.direction = "right";
            System.out.println(direction);
        }
    }

    /**
     * This method is called when a key is released on the keyboard.
     *
     * @param event which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.direction = null;
        int keyCode = e.getKeyCode();
        if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("exit the game, the current game state will be lost, the next time the game is\r\n"
                    + "started, it will resume from the last unfinished level");
        } else if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("exit the game, saves the game state, game will resume next time the\r\n"
                    + "application will be started");
        } else if ((e.getKeyCode() == KeyEvent.VK_R) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("resume saved game");
        } else if ((e.getKeyCode() == KeyEvent.VK_P) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("start a new game at the last unfinished level");
        } else if ((e.getKeyCode() == KeyEvent.VK_1) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("start a new game at level 1");
        } else if ((e.getKeyCode() == KeyEvent.VK_P) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("start a new game at the last unfinished level");
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = true;
            System.out.println("pause the game and display a “game is paused” dialog");
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = false;
            System.out.println("close the “game is paused” dialog and resume the game");
        } 

    }

    /**
     * This method is called when a key is typed on the keyboard.
     *
     * @param event which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (key == 'w') {
            this.direction = "up";
            System.out.println(direction);
        } else if (key == 'a') {
            this.direction = "left";
            System.out.println(direction);
        } else if (key == 's') {
            this.direction = "down";
            System.out.println(direction);
        } else if (key == 'd') {
            this.direction = "right";
            System.out.println(direction);
        }
    }

    // ===================================================.
    // Getters and Setters
    // ===================================================.

    /**
     * Gets the game panel.
     * 
     * @return the game panel
     */

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /**
     * Gets the score panel.
     *
     * @return the score panel
     */
    public ScorePanel getScorePanel() {
        return scorePanel;
    }

    /**
     * Checks if is recording.
     *
     * @return true, if is recording
     */
    public boolean isRecording() {
        return recording;
    }

    /**
     * Sets the recording.
     *
     * @param recording the new recording
     */
    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    /**
     * Checks if is paused.
     *
     * @return true, if is recording
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets paused.
     *
     * @param recording the new recording
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Gets the movement direction.
     *
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction.
     *
     * @param direction the new direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
}