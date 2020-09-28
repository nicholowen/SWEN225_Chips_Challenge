package nz.ac.vuw.ecs.swen225.gp20.render;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.ScorePanel;

public class Render {

  GamePanel gp;
  ScorePanel sp;

  /**
   * Constuctor
   * @param gp The Panel which the map is drawn
   * @param sp The Panel on which the score (time inventory etc is drawn)
   */
  public Render(GamePanel gp, ScorePanel sp){
    this.gp = gp;
    this.sp = sp;
  }

  /**
   * Updates the graphics based on the player position and state of the game (time inventory etc)
   * @param tuple Contains the current state of the cells and the player
   */
  public void update(RenderTuple tuple, int timeRemaining){
    gp.update(tuple);
    sp.update(timeRemaining);
  }

  /**
   * Initialises all the assets in the game.
   * This is required so the renderer has knowledge of what graphics are where at the start of the game.
   * @param cells
   */
  public void init(Cell[][] cells){
    gp.initAnimationObjects(cells);
  }

}
