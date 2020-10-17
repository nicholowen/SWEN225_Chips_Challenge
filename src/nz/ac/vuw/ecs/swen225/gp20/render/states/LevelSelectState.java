package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Handles the level selection screen.
 * @author Owen N
 */
public class LevelSelectState {
//button positions
// new game - 377, 280, 135, 21
// load -     377, 348, 135, 21
// replay -   377, 416, 135, 21
// exit -     377, 484, 135, 21

  BufferedImage bg;

  public LevelSelectState(){
    this.bg = Assets.LEVEL;
  }

  /**
   * Updates, thus repaints the graphics object.
   * TODO: include button detection so the button can be animated (highlighted and/or depressed)
   */
  public void update(){
  }


  public void draw(Graphics g) {

    g.drawImage(bg, 0, 0, null);
  }
}