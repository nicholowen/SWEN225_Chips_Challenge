package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.*;


import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;

/**
 * This class handles the setup of the main frame. It also handles the key
 * listeners and buttons as user input.
 * 
 * @author Maiza Rehan 300472305
 */
public class GUI extends JPanel implements KeyListener {

    JFrame frame;


    //=========================
    BufferedImage image;
    Graphics2D g;

    private int gameState = 0;
    //=========================



    private boolean recording = false;
    private boolean paused = false;
    private Direction direction = null;
    private boolean saveState = false;
    private String loadingState = null;

    JLayeredPane mainPanel;
    /**
     * Instantiates a new gui.
     */
    public GUI() {

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

        JButton    one = new JButton();
        JButton    two = new JButton();
        JButton  three = new JButton();
        JButton   four = new JButton();

        JButton  pause = new JButton();
        JButton record = new JButton();

        one.setBounds  (377, 280, 135, 21);
        two.setBounds  (377, 348, 135, 21);
        three.setBounds(377, 416, 135, 21);
        four.setBounds (377, 484, 135, 21);

        record.setBounds(752, 35, 22, 27);
        pause.setBounds(672, 539, 102, 22);

        mainPanel.add(   one);
        mainPanel.add(   two);
        mainPanel.add( three);
        mainPanel.add(  four);
        mainPanel.add( pause);
        mainPanel.add(record);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/icon.png").getImage());
        frame.pack();

        //========================
        // BUTTON ACTION LISTENERS
        //========================

        one.addActionListener(e -> {
//            if(gameState == 1){
//                setGameState(4);
//            }
            switch(gameState){
                case 1: //menu state
                    setGameState(2);
                    break;
                case 2: //level select state
                    setGameState(4); // start at level 1
                    break;
                case 3: //pause state
                    setGameState(4);
                    break;
            }
        });

        two.addActionListener(e -> {
            switch(gameState){
                case 1: //menu state
                    //load from saved file
                case 2: //level select state
                    setGameState(4); // start at level 2
                    break;
                case 3://pause state
                    setGameState(1); //return to main menu
                    break;
            }
        });

        three.addActionListener(e -> {
            switch(gameState){
                case 1: //menu state
                    //load replay from saved file
                case 2: //level select state
                    // no button available
                    break;
                case 3://pause state
                    // no button available
                    break;
            }
        });

        four.addActionListener(e -> {
            switch(gameState){
                case 1: //menu state
                    System.exit(0);
                case 2: //level select state
                    //no button
                    break;
                case 3://pause state
                    //no button
                    break;
            }
        });

        pause.addActionListener(e -> {
            if(gameState == 4){
                setGameState(3);
            }
        });

        record.addActionListener(e ->{
            if(gameState == 4){
                //start recording
            }
        });


    }

    public Graphics2D getImageGraphics(){
        return g;
    }

    public void drawToScreen(){
        Graphics g2 = mainPanel.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
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
        if(gameState == 4) {
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
        // pause the game and display a game is paused dialog
        else if (keyCode == KeyEvent.VK_SPACE) {
            paused = true;
        }
        // close the œgame is paused dialog and resume the game
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = false;
        }

    }

    /**
     * This method is called when a key is typed on the keyboard.
     *
     * @param KeyEvent which indicates that a keystroke occurred in a Game Panel.
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
     * Sets the current state of the game.
     *
     */
    public void setGameState(int id){
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
    public int getGameState(){
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
    public Direction getDirection() {
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