package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
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
 * This class handles the setup of the main frame. It also handles the key
 * listeners and buttons as user input.
 * 
 * @author Maiza
 */
public class GUI implements KeyListener {

    JFrame frame;
    GamePanel gamePanel;
    ScorePanel scorePanel;

    private boolean recording = false;
    private boolean paused = false;
    private String direction = null;
    private boolean saveState = false;
    private String loadingState = null;

    JLayeredPane mainPanel;
    JButton pausenplay;

    /**
     * Instantiates a new gui.
     */
    public GUI() {
        frame = new JFrame("Indecision Games: Chip's Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(800, 500));

        frame.setFocusable(true);
        frame.addKeyListener(this);

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

        pausenplay = new JButton("pause");
        JButton save = new JButton("save state");
        JButton load = new JButton("load state");
        menu.add(pausenplay);
        menu.add(save);
        menu.add(load);
        this.formatButton(pausenplay);
        this.formatButton(save);
        this.formatButton(load);

        pausenplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (paused) {
                    paused = false;
                    pausenplay.setText("pause");
                } else {
                    paused = true;
                    pausenplay.setText("play");
                }
            }
        });
        pausenplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                saveState = true;
                loadingState = "resume";
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/icon.png").getImage());
        frame.pack();

    }

    // =======================================================.
    // Utility Methods
    // =======================================================.

    /**
     * Makes a button transparent and formats the text colour. This method makes it
     * easier to format all buttons because it calls on the same functions.
     *
     * @param JButton to be formatted
     */
    public void formatButton(JButton button) {
        button.setForeground(new Color(107, 201, 240));
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
    }

    // =======================================================.
    // Key Listeners
    // =======================================================.

    /**
     * This method is called when a key is pressed on the keyboard.
     *
     * @param KeyEvent which indicates that a keystroke occurred in a Game Panel.
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
     * @param KeyEvent which indicates that a keystroke occurred in a Game Panel.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.direction = null;
        int keyCode = e.getKeyCode();
        boolean ctrl = (e.getModifiers() & KeyEvent.CTRL_MASK) != 0;
        // exit the game, the current game state will be lost, the next time the game is
        // started, it will resume from the last unfinished level
        if ((keyCode == KeyEvent.VK_X) && ctrl) {
            saveState = true;
            loadingState = "unfinished";
        }
        // exit the game, saves the game state, game will resume next time the
        // application will be started
        else if ((keyCode == KeyEvent.VK_S) && ctrl) {
            saveState = true;
            loadingState = "resume";
        }
        // resume saved game
        else if ((keyCode == KeyEvent.VK_R) && ctrl) {
            loadingState = "resume";
        }
        // start a new game at the last unfinished level
        else if ((keyCode == KeyEvent.VK_P) && ctrl) {
            loadingState = "unfinished";
        }
        // start a new game at level 1
        else if ((keyCode == KeyEvent.VK_1) && ctrl) {
            loadingState = "lvl 1";
        }
        // pause the game and display a “game is paused” dialog
        else if (keyCode == KeyEvent.VK_SPACE) {
            paused = true;
            pausenplay.setText("play");
        }
        // close the “game is paused” dialog and resume the game
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = false;
            pausenplay.setText("pause");
        }

    }

    /**
     * This method is called when a key is typed on the keyboard.
     *
     * @param KeyEvent which indicates that a keystroke occurred in a Game Panel.
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
     * Checks if is paused.
     *
     * @return true, if is recording
     */
    public boolean isPaused() {
        return paused;
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
     * Checks if is user wants to save state.
     *
     * @return true, if is saving
     */
    public boolean isSaving() {
        return saveState;
    }

    /**
     * Sets saving once saving is complete
     * 
     *
     */
    public void saved(Boolean saved) {
        this.saveState = !saved;
    }

    /**
     * Checks what state the game is to be loaded in
     *
     * @return unfinished, if is last 
     *         unfinished level lvl 1, if is first level
     *         resume, if is resuming a game
     *         null, if is no need to load
     */
    public String getLoadState() {
        return loadingState;
    }

    /**
     * Sets saving once loading is complete
     * 
     *
     */
    public void setLoadState(String loaded) {
        this.loadingState = loaded;
    }
}