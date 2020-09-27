package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.ScorePanel;

// TODO: Auto-generated Javadoc
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
     * @param key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Unused
    }

    /**
     * This method is called when a key is released on the keyboard.
     *
     * @param key released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.direction = null;
    }

    /**
     * This method is called when a key is typed on the keyboard.
     *
     * @param key typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        switch (key) {
        case 'w':
            this.direction = "up";
            System.out.println(direction);
            break;
        case 'a':
            this.direction = "left";
            System.out.println(direction);
            break;
        case 's':
            this.direction = "down";
            System.out.println(direction);
            break;
        case 'd':
            this.direction = "right";
            System.out.println(direction);
            break;
        case 'r':
            recording = !recording;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
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
     * Checks if is recording.
     *
     * @return true, if is recording
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the recording.
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
