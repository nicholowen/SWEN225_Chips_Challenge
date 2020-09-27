package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;

public class KeyListeners implements KeyListener {

    private boolean recording = false;
    private String direction = null;

    /**
     * Instantiates a new key listeners.
     *
     * @param game panel
     */
    public KeyListeners(GamePanel gp) {
        gp.addKeyListener(this);
    }

    /**
     * Key pressed.
     *
     * @param which key is pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Unused
    }

    /**
     * Key released.
     *
     * @param which key is released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.direction = null;
    }

    /**
     * Key typed.
     *
     * @param which key is typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        switch (key) {
        case 'w':
            this.direction = "up";
            System.out.println(direction);
        case 'a':
            this.direction = "left";
            System.out.println(direction);
        case 's':
            this.direction = "down";
            System.out.println(direction);
        case 'd':
            this.direction = "right";
            System.out.println(direction);
        case 'r':
            recording = !recording;

        }
    }

    /**
     * =================================================== 
     * Getters and Setters
     * ===================================================
     */

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
     * @param boolean whether or not it is recording
     */
    public void setRecording(boolean recording) {
        this.recording = recording;
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
     * @param the new direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

}
