package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.*;


import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndPlay;

/**
 * This class handles the setup of the main frame. It also handles the key
 * listeners and buttons as user input.
 * 
 * @author Maiza Rehan 300472305
 */
public class GUI extends JPanel implements KeyListener {

    JFrame frame;
    private Graphics2D g;
    private JLayeredPane mainPanel;
    // =======================================.
    private MouseManager mm;
    private JButton one;
    private JButton two;
    private JButton three;
    private JButton four;
    private JButton pause;
    private JButton record;
    // =======================================.
    private BufferedImage image;
    private String buttonSoundEvent;
    private int gameState = 0;
    // =======================================.
    private Direction direction = null;
    private boolean recording = false;
    
    private Main main;

    /**
     * Instantiates a new gui.
     */
    public GUI(Main main) {
        
        this.main = main;

        this.isFocusable();

        frame = new JFrame("Indecision Games: Chip's Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(892, 615));

        frame.setFocusable(true);
        frame.addKeyListener(this);

        image = new BufferedImage(892, 576, 1);
        g = (Graphics2D) image.getGraphics();

        mainPanel = new JLayeredPane();

        mainPanel.setLayout(null);
        frame.setContentPane(mainPanel);

        one = new JButton();
        two = new JButton();
        three = new JButton();
        four = new JButton();
        pause = new JButton();
        record = new JButton();

        mm = new MouseManager(this);
        this.formatButton(one, "one", 377, 280, 135, 21);
        this.formatButton(two, "two", 377, 348, 135, 21);
        this.formatButton(three, "three", 377, 416, 135, 21);
        this.formatButton(four, "four", 377, 484, 135, 21);

        this.formatButton(record, "record", 752, 35, 22, 27);
        this.formatButton(pause, "pause", 672, 539, 102, 22);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/icon.png").getImage());
        frame.pack();

        // =======================================.
        // Button Action Listeners
        // =======================================.

        one.addActionListener(e -> {
//            if(gameState == 1){
//                setGameState(4);
//            }
            switch (gameState) {
            case 1: // menu state
                setGameState(2);
                break;
            case 2: // level select state
                setGameState(4); // start at level 1
                break;
            case 3: // pause state
                setGameState(4);
                break;
            default:
                break;
            }
        });

        two.addActionListener(e -> {
            switch (gameState) {
            case 1: // menu state
                // load from saved file
            case 2: // level select state
                setGameState(4); // start at level 2
                break;
            case 3:// pause state
                setGameState(1); // return to main menu
                break;
            }
        });

        three.addActionListener(e -> {
            switch (gameState) {
            case 1: // menu state
                // load replay from saved file
            case 2: // level select state
                // no button available
                break;
            case 3:// pause state
                   // no button available
                break;
            }
        });

        four.addActionListener(e -> {
            switch (gameState) {
            case 1: // menu state
                System.exit(0);
            case 2: // level select state
                // no button
                break;
            case 3:// pause state
                   // no button
                break;
            }
        });

        pause.addActionListener(e -> {
            if (gameState == 4) {
                setGameState(3);
            }
        });

        record.addActionListener(e -> {
            if (gameState == 4) {
                recording = !recording;
                if(recording) {
                    try {
                        main.stopRecord();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    ;
                }
                else main.startRecord();
            }
        });

    }

    // =======================================================.
    // Utility Methods
    // =======================================================.

    /**
     * Draw to screen.
     */
    public void drawToScreen() {
        Graphics g2 = mainPanel.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    /**
     * Called to format buttons and to add them to the main panel.
     *
     * @param button the button
     * @param name   of the button
     * @param x      coordinate for button
     * @param y      coordinate for button
     * @param w      width of button
     * @param h      height of button
     */
    public void formatButton(JButton button, String name, int x, int y, int w, int h) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setRolloverEnabled(false);
        button.addMouseListener(mm);
        button.setName(name);
        button.setBounds(x, y, w, h);
        mainPanel.add(button);
    }

    // =======================================================.
    // Key Listeners
    // =======================================================.

    /**
     * This method is called when a key is pressed on the keyboard.
     *
     * @param e, Key Pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameState == 4) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP) {
                this.direction = Direction.UP;
                System.out.println(direction);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                this.direction = Direction.LEFT;
                System.out.println(direction);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                this.direction = Direction.DOWN;
                System.out.println(direction);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                this.direction = Direction.RIGHT;
                System.out.println(direction);
            }
        }
    }

    /**
     * This method is called when a key is released on the keyboard.
     *
     * @param e, Key Released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.direction = null;
        int keyCode = e.getKeyCode();
        boolean ctrl = (e.getModifiers() & KeyEvent.CTRL_MASK) != 0;
        if(this.gameState == 4) {
            // exit the game, the current game state will be lost, the next time the game is
            // started, it will resume from the last unfinished level
            if ((keyCode == KeyEvent.VK_X) && ctrl) {
                main.saveUnfinished();
                System.exit(0);
            }
            // exit the game, saves the game state, game will resume next time the
            // application will be started
            else if ((keyCode == KeyEvent.VK_S) && ctrl) {
                main.saveCurrentState();
                System.exit(0);
            }
            // resume saved game
            else if ((keyCode == KeyEvent.VK_R) && ctrl) {
                main.loadCurrentState();
            }
            // start a new game at the last unfinished level
            else if ((keyCode == KeyEvent.VK_P) && ctrl) {
                main.loadUnfinished();
            }
            // start a new game at level 1
            else if ((keyCode == KeyEvent.VK_1) && ctrl) {
                main.loadLvl1();
            }
            // pause the game and display a game is paused dialog
            else if (keyCode == KeyEvent.VK_SPACE) {
                setGameState(3);
            }
            // close the game is paused dialog and resume the game
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                setGameState(4);
            }
            // replay recording
            else if (keyCode == KeyEvent.VK_R) {
                System.out.println("REPLAYINHGGYGGH");
                recording = false;
                try {
                    main.replay();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * This method is called when a key is typed on the keyboard.
     *
     * @param e, Key Typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (gameState == 4) {
            char key = e.getKeyChar();
            if (key == 'w') {
                this.direction = Direction.UP;
                System.out.println(direction);
            } else if (key == 'a') {
                this.direction = Direction.LEFT;
                System.out.println(direction);
            } else if (key == 's') {
                this.direction = Direction.DOWN;
                System.out.println(direction);
            } else if (key == 'd') {
                this.direction = Direction.RIGHT;
                System.out.println(direction);
            }

        }
    }

    // ===================================================.
    // Getters and Setters
    // ===================================================.

    /**
     * Gets the image graphics.
     *
     * @return the image graphics
     */
    public Graphics2D getImageGraphics() {
        return g;
    }

    /**
     * Sets the current state of the game.
     *
     * @param id the new game state
     */
    public void setGameState(int id) {
        this.gameState = id;
    }

    /**
     * Checks what state the game is currently in.
     *
     * @return int 0, if intro state 
     *             1 if menu state 
     *             2 if level select 
     *             3 if paused 
     *             4 if playing
     */
    public int getGameState() {
        return gameState;
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
     * Sets recording to false, when user wants to stoprecording.
     *
     */
    public void stopRecording() {
        recording = false;
    }

    /**
     * Gets the movement direction.
     *
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the button sound event.
     *
     * @param event, the new sound event
     */
    public void setButtonSoundEvent(String event) {
        buttonSoundEvent = event;
    }

    /**
     * Gets the button sound event.
     *
     * @return the button sound event
     */
    public String getButtonSoundEvent() {
        return buttonSoundEvent;
    }

    /**
     * Reset button event to null.
     */
    public void resetButtonEvent() {
        buttonSoundEvent = null;
    }
}