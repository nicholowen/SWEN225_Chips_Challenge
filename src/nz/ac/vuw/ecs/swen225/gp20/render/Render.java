package nz.ac.vuw.ecs.swen225.gp20.render;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.panels.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.panels.ScorePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Audio;

import java.util.HashMap;

/**
 * Main Render Module class. Simply stores the Drawing panels and performs updates to both every tick.
 *
 * @author Owen N
 */
public class Render {

  GamePanel gp;
  ScorePanel sp;
  //PausePanel pp;

  Audio audio;

  /**
   * Constuctor
   * @param gp The Panel which the map is drawn
   * @param sp The Panel on which the score (time inventory etc is drawn)
   */
  public Render(GamePanel gp, ScorePanel sp/*, PausePanel pp*/){
    this.gp = gp;
    this.sp = sp;
    //this.pp = pp;
    audio = new Audio();
  }

  /**
   * Updates the graphics based on the player position and state of the game (time inventory etc)
   * @param tuple Contains the current state of the cells and the player
   */
  public void update(RenderTuple tuple, int timeRemaining, HashMap<String, Integer> inventory/*, String button*/){
    gp.update(tuple);
    sp.update(timeRemaining, tuple);
    audio.update(tuple.getSoundEvent());
  }

  public void displayPause(){
    gp.repaint();
    //pp.repaint();
  }

  /**
   * Initialises all the assets in the game.
   * This is required so the renderer has knowledge of what graphics are where at the start of the game.
  // * @param cells
   */
  public void init(Maze maze){
    gp.initAnimationObjects(maze.getBoard());
  }

}
