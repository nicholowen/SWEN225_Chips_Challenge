package nz.ac.vuw.ecs.swen225.gp20.application;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class handles the mouse events. It is mainly used for the implementation
 * of highlights on buttons upon hover, and the depression when they are clicked.
 * 
 * @author Maiza Rehan 300472305
 */
public class MouseManager implements MouseListener {

    private GUI gui;

    /**
     * Instantiates a new mouse manager.
     *
     * @param gui
     */
    MouseManager(GUI gui) {
        this.gui = gui;
    }

    /**
     * This method is called when the mouse clicks.
     *
     * @param mouseEvent the mouse clicked
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    /**
     * This method is called when the mouse presses.
     *
     * @param mouseEvent the mouse pressed
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        System.out.println("pressed");
        String name = mouseEvent.getComponent().getName();
        if (gui.getGameState() == 4) {
            if (name.equals("pause") || name.equals("record")) {
                gui.setButtonSoundEvent("pressed_" + name);
            }
        } else if (gui.getGameState() > 0) {
            if (!name.equals("pause") && !name.equals("record")) {
                gui.setButtonSoundEvent("pressed_" + name);
            }
        }
    }

    /**
     * This method is called when the mouse releases.
     *
     * @param mouseEvent the mouse released
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        gui.resetButtonEvent();
        System.out.println("released");
    }

    /**
     * This method is called when the mouse enters an area.
     *
     * @param mouseEvent the mouse entered
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        System.out.println("entered");
        String name = mouseEvent.getComponent().getName();

        if (gui.getGameState() == 4) {
            if (name.equals("pause") || name.equals("record")) {
                gui.setButtonSoundEvent("hover_" + name);
            }
        } else if (gui.getGameState() > 0) {
            if (!name.equals("pause") && !name.equals("record")) {
                gui.setButtonSoundEvent("hover_" + name);
            }
        }
    }

    /**
     * This method is called when the mouse exits an area.
     *
     * @param mouseEvent the mouse exited
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        gui.resetButtonEvent();
        System.out.println("why here?");
        System.out.println("exited");
    }
}
