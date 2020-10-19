package nz.ac.vuw.ecs.swen225.gp20.render;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;
import nz.ac.vuw.ecs.swen225.gp20.render.states.*;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Audio;

import java.awt.*;
import java.util.HashMap;

/**
 * Main Render Module class. Simply stores the Drawing panels and performs updates to both every tick.
 *
 * @author Owen Nicholson 300130653
 */
public class Render {

  Assets assets;

  MapPane gp;
  InfoPane sp;
  IntroState is;
  MenuState ms;
  LevelSelectState ls;
  PauseState ps;


  Audio audio;

  public Render(){

    this.assets = new Assets();

    gp = new MapPane(assets);
    sp = new InfoPane(assets);
    is = new IntroState(assets);
    ls = new LevelSelectState(assets);
    ms = new MenuState(assets);
    ps = new PauseState(assets);

    audio = new Audio();
  }

  /**
   * Updates the graphics based on the player position and state of the game (time inventory etc)
   * @param tuple Contains the current state of the cells and the player
   * @param timeRemaining self explanatory
   *
   */
  public void update(RenderTuple tuple, int timeRemaining, HashMap<String, Integer> inventory/*String event*/){
    gp.update(tuple);
    sp.update(timeRemaining, tuple);

//    audio.update(tuple.getSoundEvent());
    audio.updateGame(tuple.getSoundEvent());
//    audio.updateButtons(event);
  }

  /**
   * sparate update method from above for states that don't reqire information from maze
   * @param gameState Integer representing the state
   */
  public void update(int gameState/*, String event*/){
    //temporary value
    String event = null;
    switch(gameState){
      case 0:
        is.update();
        break;
      case 1:
        ms.update(event);
        break;
      case 2:
        ls.update(event);
        break;
      case 3:
        ps.update(event);
        break;
      default:
        break;
    }

//    audio.updateButtons(event);
  }

  /**
   * Draws on the provided graphics objects.
   *
   * @param g Graphics object of the main drawing object.
   * @param gameState Integer representing game state.
   */
  public void draw(Graphics g, int gameState){
    switch(gameState){
      case 0:
        is.draw(g);
        break;
      case 1:
        ms.draw(g);
        break;
      case 2:
        ls.draw(g);
        break;
      case 3:
        ps.draw(g);
        break;
      case 4:
        gp.draw(g);
        sp.draw(g);
        break;
      default:
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
