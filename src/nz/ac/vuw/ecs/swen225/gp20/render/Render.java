package nz.ac.vuw.ecs.swen225.gp20.render;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.states.*;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Audio;

import java.awt.*;
import java.util.HashMap;

/**
 * Main Render Module class. Simply stores the Drawing panels and performs updates to both every tick.
 *
 * @author Owen N
 */
public class Render {

  MapPane gp;
  InfoPane sp;
  IntroState ip;
  MapPane.MenuPanel mp;
  LevelSelectState lp;
  PauseState pp;


  Audio audio;

  /**
   * Constuctor
//   * @param gp The Panel which the map is drawn
//   * @param sp The Panel on which the score (time inventory etc is drawn)
   * * @param ip Intro Panel
   * * @param mp Menu Panel
   * * @param lp Level Panel (level select)
   * * @param pp Pause Panel
   */
  public Render(){
    gp = new MapPane();
    sp = new InfoPane();

    ip = new IntroState();
    lp = new LevelSelectState();
    mp = new MapPane.MenuPanel();
    pp = new PauseState();


    audio = new Audio();
  }

  /**
   * Updates the graphics based on the player position and state of the game (time inventory etc)
   * @param tuple Contains the current state of the cells and the player
   * @param timeRemaining self explanatory
   * @param inventory **this is no longer needed - will remove when application module updates code.**
   *
   * Will need another parameter fed by main that will give the button that is being hovered or pressed.
   */
  public void update(RenderTuple tuple, int timeRemaining, HashMap<String, Integer> inventory/*, String button*/){
    gp.update(tuple);
    sp.update(timeRemaining, tuple/*, button*/);

    audio.update(tuple.getSoundEvent());
  }

  public void update(int gameState){
    switch(gameState){
      case 0:
        ip.update();
        break;
      case 1:
        mp.update();
        break;
      case 2:
        lp.update();
        break;
      case 3:
        pp.update();
        break;
    }
  }

  public void draw(Graphics g, int gameState){
    switch(gameState){
      case 0:
        ip.draw(g);
        break;
      case 1:
        mp.draw(g);
        break;
      case 2:
        lp.draw(g);
        break;
      case 3:
        pp.draw(g);
        break;
      case 4:
        gp.draw(g);
        sp.draw(g);
        break;
    }

  }


  /**
   * Initialises all the assets in the game.
   * This is required so the renderer has knowledge of what graphics are where at the start of the game.
  // * @param cells
   */
  public void init(Maze maze){
    gp.initAnimationObjects(maze.getBoard(), maze.getActors());
  }

}
