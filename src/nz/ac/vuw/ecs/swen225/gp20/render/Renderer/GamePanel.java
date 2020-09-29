package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.maze.cells.CellDoor;
import nz.ac.vuw.ecs.swen225.gp20.maze.cells.CellExit;
import nz.ac.vuw.ecs.swen225.gp20.maze.cells.CellKey;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;
import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Allows the game map to be rendered and animated in a 9 x 9 grid around the player.
 * This class initialises all render-able objects and updates them (if they need to be animated) every tick.
 *
 * @author Owen N
 */
public class GamePanel extends JPanel {

  //Lists to be populated with Cells surrounding player in 9x9 grid
  private Cell[][] floor = new Cell[9][9];
  private Cell[][] wall = new Cell[9][9];
  private Cell[][] door = new Cell[9][9];
  private Cell[][] energy = new Cell[9][9];
  private Cell[][] key = new Cell[9][9];

  // currently not being used (for transition animation if being implemented)
  private Cell[][] map;
  private Object[][] sprites;

  // HashMaps to store all rendered objects in the game
  private HashMap<Cell, WallTile> wallObjects = new HashMap<>();
  private HashMap<Cell, Door> doorObjects = new HashMap<>();
  private HashMap<Cell, EnergyBall> energyObjects = new HashMap<>();
  private HashMap<Cell, KeyCard> keyObjects = new HashMap<>();

  //Single Rendered object (only one per map)
  private Info info;
  private Exit exit;
  private Player playerSprite;

  private Actor player;

  public GamePanel() {
    setPreferredSize(new Dimension(576, 576));
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

  /**
   * Initialises all 'sprites' in game, based on the position of the cells.
   * Makes it easy to locate each render object and to avoid assigning them to the cells themselves.
   * @param cells The tiles of the game map.
   */
  public void initAnimationObjects(Cell[][] cells){
    sprites = new Object[cells.length][cells[0].length];
    this.map = cells;
    this.playerSprite = new Player();
    for(int i = 0; i < cells.length; i++){
      for(int j = 0; j < cells[i].length; j++){
        switch(cells[i][j].getName()){
          case "wall":
            WallTile wt = new WallTile();
            Cell[] neighbours = getWallNeighbours(cells, j, i);
            wt.setWallType(neighbours[0], neighbours[1], neighbours[2], neighbours[3]); //wall object initialised with cells at each cardinal direction
            sprites[i][j] = wt;
            wallObjects.put(cells[i][j], wt);
          case "treasure":
            EnergyBall eb = new EnergyBall();
            sprites[i][j] = eb;
            energyObjects.put(cells[i][j], eb);
          case "key":
            if(cells[i][j] instanceof CellKey) {
              KeyCard kc = new KeyCard(cells[i][j].getColor());
              sprites[i][j] = kc;
              keyObjects.put(cells[i][j], kc);
            }
          case "door":
            if(cells[i][j] instanceof CellDoor) {
              Door door = new Door(cells[i][j].getColor());
              sprites[i][j] = door;
              doorObjects.put(cells[i][j], door);
            }
          case "exit":
            if(cells[i][j] instanceof CellExit) {
              System.out.println("GOT HERE");
              exit = new Exit(i, j);
            }

        }
      }
    }
  }

  /**
   * Finds the neighbouring cells of a wall (all cardinal directions).
   * @param cells All map cells.
   * @param x the x coordinate of the main cell being checked.
   * @param y the y coordinate of the main cell being checked.
   * @return A cell array containing between 1 and 4 cells which are adjacent to the main cell in north, east, south, west order..
   */
  private Cell[] getWallNeighbours(Cell[][] cells, int x, int y){
    Cell[] neighbours  = new Cell[4];
    int lengthX = cells[0].length;
    int lengthY = cells.length;

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
   * Gets a 9x9 grid around the player. Will keep the player centered on the screen.
   * @param cells All cells for entire maze
   * @param player The player's character - for obtaining current position
   * @return 9x9 Cell array
   */
  private Cell[][] getSurround(Cell[][] cells, Actor player) {
    Cell[][] ret = new Cell[9][9]; //a new array to store all the cells around the player
    Point playerPos = new Point(player.getX(), player.getY());


    int x = (int)playerPos.getX() - 4;
    int y;

    for (int i = 0; i < 9; i++) {
      y = (int)playerPos.getY() - 4;
      for (int j = 0; j < 9; j++) {
        if((x >= 0 && x < cells.length) && (y >= 0 && y < cells[0].length)) {
          ret[i][j] = cells[x][y];

        }else{
          ret[i][j] = null;
        }
        y++;
      }
      x++;
    }
    return ret;
  }

  /**
   * Iterates through a 9x9 grid around the player. Adds them to an appropriate list
   * which is used for drawing in order. Also updates all updatable items
   * (things with animation)
   * @param tuple tuple containing Cells and actors
   */
  public void update(RenderTuple tuple) {
    player = tuple.getActors()[0];
    Cell[][] surround = getSurround(tuple.getCells(), player);

    playerSprite.update(player.getDirection());
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if(surround[i][j] != null) {
          switch (surround[i][j].getName()) {
            case "free":
              floor[i][j] = surround[i][j];
              break;
            case "wall":
              wall[i][j] = surround[i][j];
              break;
            case "door":
              door[i][j] = surround[i][j];
              if(doorObjects.containsKey(surround[i][j])){
                doorObjects.get(surround[i][j]).update();
              }
              break;
            case "treasure":
              energy[i][j] = surround[i][j];
              floor[i][j] = surround[i][j];
              if (energyObjects.containsKey(surround[i][j])) {
                energyObjects.get(surround[i][j]).update();
              }
              break;
            case "key":
              key[i][j] = surround[i][j];
              floor[i][j] = surround[i][j];
              if (keyObjects.containsKey(surround[i][j])) {
                keyObjects.get(surround[i][j]).update();
              }
              break;
            case "info":
              info = new Info(i, j, Assets.INFO[0][0]);
              break;
            case "exit":
              exit.update();
              break;
          }
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
    String[] metadata = new String[0];
    if(cell.getRenderData() != null){
      metadata = cell.getRenderData().split(":");
    }
    if(identifier.equals("name")) return metadata[0];
    else return metadata[1];
  }


  /**
   * Clears all lists ready for next frame
   */
  private void clearLists(){
    floor  = new Cell[9][9];
    wall   = new Cell[9][9];
    door   = new Cell[9][9];
    energy = new Cell[9][9];
    key    = new Cell[9][9];
  }

  /**
   * Draws all visible sprites(in the 9x9 grid around player) in order from the floor up.
   * @param g graphics object
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int x = 64;
    int y = 64;

//    System.out.println(exit.getX() +":" + exit.getY());


    g.drawImage(Assets.MAPBACKGROUND[0][0], 0, 0, this);
    //currently using static images - not retrieved from the object itself yet (it didn't work when I tried)
    for(int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {

        if(info != null && i == info.getX() && j == info.getY()){
          g.drawImage(info.getImage(), y * i, x * j, this);
        }
        if(exit != null && i == exit.getX() && j == info.getY()){
          g.drawImage(exit.getImage(), y * i, x * j, this);
        }
        if (floor[i][j] != null) {
          g.drawImage(Assets.FLOOR[0][0], i * y, x * j, this);
        }
        if (wall[i][j] != null) {
          g.drawImage(wallObjects.get(wall[i][j]).getImage(), y * i, x * j, this);
        }
        if (door[i][j] != null) {
          g.drawImage(doorObjects.get(door[i][j]).getImage(), y * i, x * j, this);
        }
        if (energy[i][j] != null) {
          g.drawImage(energyObjects.get(energy[i][j]).getImage(), y * i, x * j, this);
        }
        if (key[i][j] != null) {
          g.drawImage(keyObjects.get(key[i][j]).getImage(), y * i, x * j, this);
        }
      }
    }
    //draws player last to remain on top
    if(playerSprite != null) g.drawImage(playerSprite.getImage(), 4 * 64, 4 * 64, this);

    //clears lists for next frame
    clearLists();

  }

  //=====================================
// CURRENTLY UNUSED - UNDER DELEVOPMENT
//=====================================

//  int offset = 0;

//  private BufferedImage[] getTransitionImages(String dir){
//    BufferedImage[] transitionImages = new BufferedImage[9];
//
//    Cell[][] surround = getSurround(map, player);
//
//    int x = 0;
//    int y = 0;
//    int i = 0;
//    int k = 0;
//    int l = 0;
//
//    for(int j = 0; j < 9; j++){
//      BufferedImage image = null;
//      switch (dir) {
//        case "north":
//          offset += -4;
//          i = 0;
//          k = 0;
//          l = 64;
//          x = surround[i][j].getX();
//          y = surround[i][j].getY();
//          break;
//        case "east":
//          offset += 4;
//          i = surround[0].length;
//          k = 0;
//          l = 0;
//          x = surround[j][i].getX();
//          y = surround[j][i].getY();
//          break;
//        case "south":
//          offset += 4;
//          i = surround.length;
//          k = 0;
//          l = 0;
//          x = surround[i][j].getX();
//          y = surround[i][j].getY();
//          break;
//        case "west":
//          offset += -4;
//          i = 0;
//          k = 60;
//          l = 0;
//          x = surround[j][i].getX();
//          y = surround[j][i].getY();
//          break;
//      }
//
//      Object sprite = sprites[y-1][x-1];
//      if(sprite instanceof WallTile){
//        WallTile wt = (WallTile)sprite;
//        image = wt.getImage();
//      }else if(sprite instanceof Door) {
//        Door door = (Door) sprite;
//        image = door.getImage();
//      }else if(sprite instanceof KeyCard) {
//        KeyCard key = (KeyCard) sprite;
//        image = joinImage(key.getImage());
//      }else if(sprite instanceof EnergyBall) {
//        EnergyBall eb = (EnergyBall) sprite;
//        image = joinImage(eb.getImage());
//      }else{
//        image = Assets.FLOOR[0][0];
//      }
//      if(image != null) {
//        transitionImages[j] = image.getSubimage(k, l + offset, image.getWidth(), image.getHeight());
//      }
//    }
//    return transitionImages;
//  }
//
//  //UNTESTED - joins two images together - so the energy and key can be drawn on top of the floors.
//  private BufferedImage joinImage(BufferedImage image){
//    BufferedImage target = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
//    Graphics2D g2d = (Graphics2D) target.getGraphics();
//    g2d.drawImage(image, 0, 0, null);
//    g2d.drawImage(Assets.FLOOR[0][0], 0, 0, null);
//    g2d.dispose();
//    return target;
//  }

}