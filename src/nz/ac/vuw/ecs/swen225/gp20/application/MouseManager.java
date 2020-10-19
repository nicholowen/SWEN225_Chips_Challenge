package nz.ac.vuw.ecs.swen225.gp20.application;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseManager implements MouseListener {

  GUI gui;

  MouseManager(GUI gui){
    this.gui = gui;
  }

  @Override
  public void mouseClicked(MouseEvent mouseEvent) {

  }

  @Override
  public void mousePressed(MouseEvent mouseEvent) {
    System.out.println("pressed");
    String name = mouseEvent.getComponent().getName();
    if (gui.getGameState() == 4) {
      if (name.equals("pause") || name.equals("record")){
        gui.setButtonSoundEvent("pressed_" + name);
      }
    }
    else if (gui.getGameState() > 0){
      if (!name.equals("pause") && !name.equals("record")){
        gui.setButtonSoundEvent("pressed_" + name);
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent mouseEvent) {
    gui.resetButtonEvent();
    System.out.println("released");
  }

  @Override
  public void mouseEntered(MouseEvent mouseEvent) {
    System.out.println("entered");
    String name = mouseEvent.getComponent().getName();

    if (gui.getGameState() == 4) {
      if (name.equals("pause") || name.equals("record")){
        gui.setButtonSoundEvent("hover_" + name);
      }
    }
    else if (gui.getGameState() > 0){
      if (!name.equals("pause") && !name.equals("record")){
        gui.setButtonSoundEvent("hover_" + name);
      }
    }
  }

  @Override
  public void mouseExited(MouseEvent mouseEvent) {
    gui.resetButtonEvent();
    System.out.println("why here?");
    System.out.println("exited");
  }
}
