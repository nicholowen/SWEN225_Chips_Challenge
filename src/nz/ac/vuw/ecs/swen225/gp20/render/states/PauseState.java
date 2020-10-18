package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * Handles the pause screen.
 * @author Owen Nicholson 300130653
 */
public class PauseState {

BufferedImage bg;

  public PauseState(){
    this.bg = Assets.PAUSE;
  }

  /**
   * Updates, thus repaints the graphics object.
   * TODO: include button detection so the button can be animated (highlighted and/or depressed)
   */

  public void update(){}

  public void draw(Graphics g) {
    g.drawImage(bg, 0, 0, null);
  }

}