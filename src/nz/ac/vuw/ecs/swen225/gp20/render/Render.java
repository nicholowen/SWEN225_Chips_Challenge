package nz.ac.vuw.ecs.swen225.gp20.render;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.ScorePanel;

public class Render {

  GamePanel gp;
  ScorePanel sp;

  public Render(GamePanel gp, ScorePanel sp){
    this.gp = gp;
    this.sp = sp;
  }

  public void update(RenderTuple tuple){
    gp.update(tuple);
  }

  public void init(Cell[][] cells){
    gp.initAnimationObjects(cells);
  }

}
