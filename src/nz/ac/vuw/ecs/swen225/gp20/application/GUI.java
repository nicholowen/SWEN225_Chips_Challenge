package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndPlay;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
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
    private static String direction = null;
//    JLayeredPane layeredPane;

    public enum Direction {
        up, down, left, right;
    }

    /**
     * Instantiates a new gui.
     */
    public GUI() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setLayout(new GridBagLayout());
        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.add(new ScorePanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();

        gamePanel.addKeyListener(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * =======================================================. 
     * Key Listeners
     * =======================================================.
     */

    /**
     * This method is called when a key is pressed on the keyboard.
     *
     * @param event which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Unused
    }

    /**
     * This method is called when a key is released on the keyboard.
     *
     * @param event which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.direction = null;

    }

    /**
     * This method is called when a key is typed on the keyboard.
     *
     * @param event which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        int keyCode = e.getKeyCode();

        if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("exit the game, the current game state will be lost, the next time the game is\r\n"
                    + "started, it will resume from the last unfinished level");
        } else if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("exit the game, saves the game state, game will resume next time the\r\n" +
                    "application will be started");
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
            System.out.println("pause the game and display a �game is paused� dialog");
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = false;
            System.out.println("close the �game is paused� dialog and resume the game");
        } else if (key == 'w' || keyCode == KeyEvent.VK_UP) {
            this.direction = "up";
            System.out.println(direction);
        } else if (key == 'a' || keyCode == KeyEvent.VK_LEFT) {
            this.direction = "left";
            System.out.println(direction);
        } else if (key == 's' || keyCode == KeyEvent.VK_DOWN) {
            this.direction = "down";
            System.out.println(direction);
        } else if (key == 'd' || keyCode == KeyEvent.VK_RIGHT) {
            this.direction = "right";
            System.out.println(direction);
        }

        if (RecordAndPlay.getIsBeingRecorded()) {
            RecordAndPlay.addMovement(direction);
        }
    }

    /**
     * ===================================================. 
     * Getters and Setters
     * ===================================================.
     */

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
     * @param paused true if pausing, false otherwise
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Gets the movement direction.
     *
     * @return the direction
     */
    public static String getDirection() {
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
