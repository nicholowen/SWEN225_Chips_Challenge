package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;
import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GamePanel extends JPanel {

  //Lists to be populated with Cells surrounding player in 9x9 grid
  private ArrayList<Cell> FLOOR = new ArrayList<>();
  private ArrayList<Cell> WALL = new ArrayList<>();
  private ArrayList<Cell> DOOR = new ArrayList<>();
  private ArrayList<Cell> ENERGY = new ArrayList<>();
  private ArrayList<Cell> KEYCARD = new ArrayList<>();

  public GamePanel() {
    setPreferredSize(new Dimension(500, 500));
    setBackground(Color.gray);
    setFocusable(true);
    requestFocus();
    init();
  }

  /**
   * Initialises all objects - if they are created in other classes, can reference them here with getters.
   * I would assume all objects will be kept in arrays..?
   */
  private void init() {
    BufferedImage image = new BufferedImage(500, 500, 1);
    Graphics g = image.getGraphics();
  }

  //needs to be called when the map has been loaded and all objects have been initialised.
  //this assigns animation objects to all objects that can animate so they can be updated
  //as well as walls so they can have an associated wall orientation given.
  public void initAnimationObjects(Cell[][] cells){
    for(int i = 0; i < cells.length; i++){
      for(int j = 0; j < cells[i].length; i++){
        String[] data = cells[i][j].getRenderData().split(":");
        switch(data[0]){
          case "wall":
            WallTile wt = new WallTile();
            if(i == 0 && j == 0) wt.setWallType(null, cells[i+1][j], cells[i][j+1], null);
            else if(i == 0 && j == cells[i].length) wt.setWallType(cells[i][j-1], cells[i+1][j], null, null);
            else if(i == cells.length && j == 0) wt.setWallType(null, null, cells[i][j+1], cells[i-1][j]);
            else if(i == cells.length && j == cells[i].length) wt.setWallType(cells[i-1][j], null, null, cells[i][j-1]);
            else wt.setWallType(cells[i-1][j], cells[i+1][j], cells[i][j+1], cells[i][j-1]);
          case "energy_ball":
            EnergyBall eb = new EnergyBall();
            cells[i][j].setAnimationObject(eb);
          case "keycard_green":
            KeyCard kc = new KeyCard()
            cells[i][j].setAnimationObject(kc);
          case "door":
            Door door = new Door();
            cells[i][j].setAnimationObject(door);
        }
      }
    }
  }

  /**
   * Gets a 9x9 grid around the player. If the player is too far to the left/right, or top/bottom of the maze boundary,
   * will get the furthest 9x9 grid possible.
   * @param cells All cells for entire maze
   * @param player The players character - for obtaining current position
   * @return 9x9 Cell array
   */
  private Cell[][] getSurround(Cell[][] cells, Actor player) {
    Cell[][] ret = new Cell[9][9];
    //using actors[0] as a place holder for player. I assume the player will be the first one on the list.
    Point playerPos = new Point(actors[0].getX(), actors.getY());

    int x;
    int y;

    if (playerPos.getX() <= 4) x = 0;
    else if (playerPos.getX() >= cells.length - 4) x = cells.length - 9;
    else x = (int) (playerPos.getX() - 4);

    if (playerPos.getY() <= 4) y = 0;
    else if (playerPos.getY() >= cells[0].length - 4) y = cells[0].length - 9;
    else y = (int) (playerPos.getX() - 4);

    for (int i = x; i < x + 9; i++) {
      for (int j = y; j < y + 9; j++) {
        ret[i][j] = cells[i][j];
      }
    }
    return ret;
  }

  /**
   * Iterates through a 9x9 grid around the player. Adds them to an appropriate list
   * which is used for drawing in order. Also updates all updatable items
   * (things with animation)
   * @param cells 9x9 grid around the player
   * @param actors for finding player position
   */
  public void update(Cell[][] cells, Actor[] actors) {

    Cell[][] surround = getSurround(cells, actors[0]);

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if(surround[i][j].getName().equals("floor")) {
          FLOOR.add(surround[i][j]);
        }else if(surround[i][j].getName().equals("wall")) {
          WALL.add(surround[i][j]);
        }else if(surround[i][j].getName().equals("door")) {
          DOOR.add(surround[i][j]);
          surround[i][j].getAnimationObject().update();
        }else if(surround[i][j].getName().equals("energy")) {
          ENERGY.add(surround[i][j]);
          surround[i][j].getAnimationObject().update();
        }else if(surround[i][j].getName().equals("key")){
          KEYCARD.add(surround[i][j]);
          surround[i][j].getAnimationObject().update();
        }
      }
    }
  }


  /**
   * Clears all lists ready for next frame
   */
  private void clearLists(){
    FLOOR.clear();
    WALL.clear();
    DOOR.clear();
    ENERGY.clear();
    KEYCARD.clear();
  }


  /**
   * Draws all visible sprites(in the 9x9 grid around player) in order from the floor up.
   * @param g graphics object
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for(Cell f : FLOOR){
      g.drawImage(Assets.FLOOR, 64 * f.getX(), 64 * f.getY(), this);
    }
    for(Cell w : WALL){
      g.drawImage(w.getAnimationObject().getImage(), 64 * w.getX(), 64 * w.getY(), this);
    }
    for(Cell d : DOOR){
      g.drawImage(d.getAnimationObject().getImage(), 64 * d.getX(), 64 * d.getY(), this);
    }
    for(Cell e : ENERGY){
      g.drawImage(e.getAnimationObject().getImage(), 64 * e.getX(), 64 * e.getY(), this);
    }
    for(Cell k : KEYCARD){
      g.drawImage(k.getAnimationObject().getImage(), 64 * k.getX(), 64 * k.getY(), this);
    }

    //clears lists for next frame
    clearLists();

  }

}