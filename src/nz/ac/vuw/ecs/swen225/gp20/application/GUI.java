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
    private JButton slow;
    private JButton fast;
    private JButton pauseRecording;
    private JButton play;
    private JButton step;
    // =======================================.
    private BufferedImage image;
    private String buttonSoundEvent;
    private int gameState = 0;
    // =======================================.
    private Direction direction = null;
    private boolean recording = false;
    private int replaying = 0;

    private int lastState;
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
        mm = new MouseManager(this);

        // =======================================.
        // Menu Buttons
        // =======================================.
        // The name of these buttons represents the order in which they appear in each
        // state (i.e. one is the first button in every state)
        one = new JButton();
        two = new JButton();
        three = new JButton();
        four = new JButton();

        this.formatButton(one, "one", 377, 280, 135, 21);
        this.formatButton(two, "two", 377, 348, 135, 21);
        this.formatButton(three, "three", 377, 416, 135, 21);
        this.formatButton(four, "four", 377, 484, 135, 21);

        // =======================================.
        // Menu Button Action Listeners
        // =======================================.
        one.addActionListener(e -> { // Decides what the first button does in each state
            switch (gameState) {
            case 1: // menu state
                setGameState(4);
                break;
            case 2: // level select state
                main.loadLvl(1);
                setGameState(4); // start at level 1
                break;
            case 3: // pause state
                setGameState(4);
                break;
            case 5: // loss state
                main.loadUnfinished();
                setGameState(4);
                break;
            case 6: // win state
                main.loadUnfinished();
                setGameState(1);
                break;
            case 7: // info state
                // no button
                break;
            }
        });

        two.addActionListener(e -> { // Decides what the second button does in each state
            switch (gameState) {
            case 1: // menu state
                setGameState(2);
                break;
            case 2: // level select state
                main.loadLvl(2);
                setGameState(4); // start at level 2
                break;
            case 3:// pause state
                main.saveUnfinished();
                main.loadUnfinished();
                setGameState(4); // restart last unfinished level
                break;
            case 5: // loss state
                main.loadUnfinished();
                setGameState(1); // back to main menu
                break;
            case 6: // win state
                System.exit(0); // exit the game
            case 7: // info state
                // no button
                break;
            }
        });

        three.addActionListener(e -> { // Decides what the third button does in each state
            switch (gameState) {
            case 1: // menu state
                System.exit(0); // exit the game
            case 2: // level select state
                setGameState(1);
                break;
            case 3:// pause state
                main.saveCurrentState(); // save state
                break;
            case 5: // loss state
                System.exit(0); // exit the game
            case 6: // win state
                // no button
                break;
            case 7: // info state
                // no button
                break;
            }
        });

        four.addActionListener(e -> { // Decides what the fourth button does in each state
            switch (gameState) {
            case 1: // menu state
                // no button
                break;
            case 2: // level select state
                // no button
                break;
            case 3:// pause state
                setGameState(1); // go to main menu
                break;
            case 5:// loss state
                   // no button
                break;
            case 6:// win state
                   // no button
                break;
            case 7:// info state
                setGameState(lastState);
                break;
            }
        });

        // =======================================.
        // In-Game Buttons
        // =======================================.

        pause = new JButton();
        record = new JButton();

        slow = new JButton();
        fast = new JButton();
        pauseRecording = new JButton();
        play = new JButton();
        step = new JButton();

        this.formatButton(record, "record", 752, 35, 22, 27);
        this.formatButton(pause, "pause", 672, 539, 102, 22);

        this.formatButton(slow, "slow", 615, 80, 18, 18);
        this.formatButton(fast, "fast", 645, 80, 18, 18);
        this.formatButton(pauseRecording, "pauseRecording", 675, 80, 18, 18);
        this.formatButton(play, "play", 705, 80, 18, 18);
        this.formatButton(step, "step", 735, 80, 18, 18);

        // =======================================.
        // In-Game Button Action Listeners
        // =======================================.

        pause.addActionListener(e -> {
            if (gameState == 4) {
                setGameState(3);
            } else if (gameState == 1) {
                lastState = 1;
                setGameState(7); // show game info (instructions)
            } else if (gameState == 3) {
                lastState = 3;
                setGameState(7); // show game info (instructions)
            }
        });

        record.addActionListener(e -> {
            if (gameState == 4) {
                if (recording) {
                    try {
                        main.stopRecord();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    ;
                } else
                    main.startRecord();
                recording = !recording;
            }
        });

        slow.addActionListener(e -> {
            if (gameState == 4) {
                if (main.getSpeed() == 100) {
                    main.setSpeed(300);
                } else if (main.getSpeed() == 300) {
                    main.setSpeed(500);
                } else if (main.getSpeed() == 500) {
                    main.setSpeed(1000);
                } else {
                    return;
                }
            }
        });

        fast.addActionListener(e -> {
            if (gameState == 4) {
                System.out.println(main.getSpeed());
                if (main.getSpeed() == 1000) {
                    main.setSpeed(500);
                } else if (main.getSpeed() == 500) {
                    main.setSpeed(300);
                } else if (main.getSpeed() == 300) {
                    main.setSpeed(100);
                } else {
                    return;
                }
            }
        });

//        pauseRecording.addActionListener(e -> {
//            if (gameState == 4) {
//                replaying = 2;
//            }
//        });
//
        play.addActionListener(e -> {
            if (gameState == 4) {
                recording = false;
                try {
                    main.replay();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        step.addActionListener(e -> {
            if (gameState == 4) {
                try {
                    main.step();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                ;
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
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
            } else if (keyCode == KeyEvent.VK_LEFT) {
                this.direction = Direction.LEFT;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                this.direction = Direction.DOWN;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                this.direction = Direction.RIGHT;
            }
            if (recording) {
                main.movePlayer(direction.toString());
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
        if (this.gameState == 4) {
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
                main.loadLvl(1);
            }
            // pause the game and display a game is paused dialog
            else if (keyCode == KeyEvent.VK_SPACE) {
                setGameState(3);
            }
            // replay recording
            else if (keyCode == KeyEvent.VK_R) {
                recording = false;
                try {
                    main.replay();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (this.gameState == 3) {
            // close the game is paused dialog and resume the game
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.out.println("ESCAPE");
                setGameState(4);
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
            } else if (key == 'a') {
                this.direction = Direction.LEFT;
            } else if (key == 's') {
                this.direction = Direction.DOWN;
            } else if (key == 'd') {
                this.direction = Direction.RIGHT;
            }
            if (recording) {
                main.movePlayer(direction.toString());
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
     * @return 0, if intro state 
     *         1, if menu state 
     *         2, if level select 
     *         3, if paused 
     *         4, if playing 
     *         5, if game lost state 
     *         6, if game won state 
     *         7, if info state
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