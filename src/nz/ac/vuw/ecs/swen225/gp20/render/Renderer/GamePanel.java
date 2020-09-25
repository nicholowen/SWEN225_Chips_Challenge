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
import java.util.HashMap;

public class GamePanel extends JPanel {

  //Lists to be populated with Cells surrounding player in 9x9 grid
  private Cell[][] floor = new Cell[9][9];
  private Cell[][] wall = new Cell[9][9];
  private Cell[][] door = new Cell[9][9];
  private Cell[][] energy = new Cell[9][9];
  private Cell[][] key = new Cell[9][9];

  private HashMap<Cell, WallTile> wallObjects = new HashMap<>();
  private HashMap<Cell, Door> doorObjects = new HashMap<>();
  private HashMap<Cell, EnergyBall> energyObjects = new HashMap<>();
  private HashMap<Cell, KeyCard> keyObjects = new HashMap<>();

  private Actor player;
  private EnergyBall eg = new EnergyBall();
  private KeyCard kc = new KeyCard();

  public GamePanel() {
    setPreferredSize(new Dimension(585, 585));
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
      for(int j = 0; j < cells[i].length; j++){
        String[] data = cells[i][j].getRenderData().split(":");
        switch(data[0]){
          case "wall":
            WallTile wt = new WallTile();
            Cell[] neighbours = getWallNeighbours(cells, j, i, cells[0].length, cells.length);
            wt.setWallType(neighbours[0], neighbours[1], neighbours[2], neighbours[3]);
            wallObjects.put(cells[i][j], wt);
          case "treasure":
            EnergyBall eb = new EnergyBall();
            energyObjects.put(cells[i][j], eb);
          case "key":
            KeyCard kc = new KeyCard();
            keyObjects.put(cells[i][j], kc);
          case "door":
            Door door = new Door();
            doorObjects.put(cells[i][j], door);
        }
      }
    }
  }

  private Cell[] getWallNeighbours(Cell[][] cells, int x, int y, int lengthX, int lengthY){
    Cell[] neighbours  = new Cell[4];

    if(x != 0){
      neighbours[1] = cells[y][x-1];
      if(x < lengthX-1)neighbours[3] = cells[y][x+1];
      else neighbours[3] = null;
    }else{
      neighbours[1] = null;
      neighbours[3] = cells[y][x+1];
    }
    if(y != 0){
      neighbours[0] = cells[y-1][x];
      if(y < lengthY-1) neighbours[2] = cells[y+1][x];
      else neighbours[2] = null;
    }else{
      neighbours[0] = null;
      neighbours[2] = cells[y+1][x];
    }

    return neighbours;
  }

  /**
   * Gets a 9x9 grid around the player. If the player is too far to the left/right, or top/bottom of the maze boundary,
   * will get the furthest 9x9 grid possible.
   * @param cells All cells for entire maze
   * @param actors The players character - for obtaining current position
   * @return 9x9 Cell array
   */
  private Cell[][] getSurround(Cell[][] cells, Actor[] actors) {
    Cell[][] ret = new Cell[9][9];
    //using actors[0] as a place holder for player. I assume the player will be the first one on the list.
    Point playerPos = new Point(actors[0].getX(), actors[0].getY());

    int x;
    int y;

    if (playerPos.getX() < 5) x = 0;
    else if (playerPos.getX() > cells[0].length - 4) x = cells.length - 9;
    else x = (int) (playerPos.getX() - 5); //dont understand why this is 5 (see next note)

    if (playerPos.getY() < 5) y = 0;
    else if (playerPos.getY() > cells.length - 4) y = cells[0].length - 9;
    else y = (int) (playerPos.getY() - 3); //and this is 3... :s

    int tempx = x;
    int tempy = y;

    for (int i = 0; i < 9; i++) {
      tempx = x;
      for (int j = 0; j < 9; j++) {
        ret[i][j] = cells[tempy][tempx];
        tempx++;
      }
      tempy++;
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
    player = actors[0];
    Cell[][] surround = getSurround(cells, actors);

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        switch (getInfo("name", surround[i][j])) {
          case "free":
            floor[i][j] = surround[i][j];
            break;
          case "wall":
            wall[i][j] = surround[i][j];
            break;
          case "door":
            door[i][j] = surround[i][j];
//            (Door)surround[i][j].getAnimationObject();
            break;
          case "treasure":
            energy[i][j] = surround[i][j];
            floor[i][j] = surround[i][j];
            if(energyObjects.containsKey(surround[i][j])){
              energyObjects.get(surround[i][j]).update();
            }
            break;
          case "key":
            key[i][j] = surround[i][j];
            floor[i][j] = surround[i][j];
            if(keyObjects.containsKey(surround[i][j])){
              keyObjects.get(surround[i][j]).update();
            }
            break;
        }
      }
    }
    repaint();
  }

  /**
   * Gets meta data from cell
   * @param identifier determines which part of the metadata to extract
   * @param cell the cell with the meta data
   * @return if identifier is "name" return the name of the cell, otherwise return the animation frame (for syncing)
   */
  private String getInfo(String identifier, Cell cell){
    String[] metadata = cell.getRenderData().split(":");
    if(identifier.equals("name")) return metadata[0];
    else return metadata[1];
  }


  /**
   * Clears all lists ready for next frame
   */
  private void clearLists(){
    floor = new Cell[9][9];
    wall = new Cell[9][9];
    door = new Cell[9][9];
    energy = new Cell[9][9];
    key = new Cell[9][9];

  }

  public Point translate(Cell cell){
    Point c = new Point(cell.getX(), cell.getY());
    int x = -(player.getX() - 4);
    int y = -(player.getY() - 4);
    c.translate(x, y);
    return c;
  }


  /**
   * Draws all visible sprites(in the 9x9 grid around player) in order from the floor up.
   * @param g graphics object
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //currently using static images - not retrieved from the object itself yet (it didn't work when I tried)
    for(int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (floor[i][j] != null) {
          g.drawImage(Assets.FLOOR[0][0], 64 * i, 64 * j, this);
        }
        if (wall[i][j] != null) {
          g.drawImage(wallObjects.get(wall[i][j]).getImage(), 64 * i, 64 * j, this);
        }
        if (door[i][j] != null) {
          g.drawImage(Assets.DOOR[0][0], 64 * i, 64 * j, this);
        }
        if (energy[i][j] != null) {
          g.drawImage(energyObjects.get(energy[i][j]).getImage(), 64 * i, 64 * j, this);
        }
        if (key[i][j] != null) {
          g.drawImage(keyObjects.get(key[i][j]).getImage(), 64 * i, 64 * j, this);
        }
      }
    }

    //clears lists for next frame
    clearLists();

  }

}