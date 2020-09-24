package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.Animation;

public abstract class Sprite {

  // TODO: Introduce location information for the sprites, provided by the Maze module.
  //       To position all objects on the screen.

  Animation animation;

  public Sprite(Maze map){
    animation = new Animation();
  }
}
