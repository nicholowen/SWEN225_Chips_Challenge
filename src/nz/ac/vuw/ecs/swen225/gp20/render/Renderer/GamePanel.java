package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
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

  private Cell[][] map;
  private Object[][] sprites;

  private HashMap<Cell, WallTile> wallObjects = new HashMap<>();
  private HashMap<Cell, Door> doorObjects = new HashMap<>();
  private HashMap<Cell, EnergyBall> energyObjects = new HashMap<>();
  private HashMap<Cell, KeyCard> keyObjects = new HashMap<>();
  private Info info;
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

  //needs to be called when the map has been loaded and all objects have been initialised.
  //this assigns animation objects to all objects that can animate so they can be updated
  //as well as walls so they can have an associated wall orientation given.
  public void initAnimationObjects(Cell[][] cells){
    sprites = new Object[cells.length][cells[0].length];
    this.map = cells;
    this.playerSprite = new Player();
    for(int i = 0; i < cells.length; i++){
      for(int j = 0; j < cells[i].length; j++){
        String[] data = cells[i][j].getRenderData().split(":");
        switch(data[0]){
          case "wall":
            WallTile wt = new WallTile();
            Cell[] neighbours = getWallNeighbours(cells, j, i, cells[0].length, cells.length);
            wt.setWallType(neighbours[0], neighbours[1], neighbours[2], neighbours[3]);
            sprites[i][j] = wt;
            wallObjects.put(cells[i][j], wt);
          case "treasure":
            EnergyBall eb = new EnergyBall();
            sprites[i][j] = eb;
            energyObjects.put(cells[i][j], eb);
          case "key":
            KeyCard kc = new KeyCard();
            sprites[i][j] = kc;
            keyObjects.put(cells[i][j], kc);
          case "door":
            Door door = new Door();
            sprites[i][j] = door;
            doorObjects.put(cells[i][j], door);

        }
      }
    }
  }

  /**
   * Finds the neighbouring cells of a wall (all cardinal directions)
   * @param cells
   * @param x
   * @param y
   * @param lengthX
   * @param lengthY
   * @return
   */
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
//   * @param actors The players character - for obtaining current position
   * @return 9x9 Cell array
   */
  private Cell[][] getSurround(Cell[][] cells, Actor player) {
    Cell[][] ret = new Cell[9][9];
    //using actors[0] as a place holder for player. I assume the player will be the first one on the list.
    Point playerPos = new Point(player.getX(), player.getY());

    int x;
    int y;

    if (playerPos.getX() < 5) x = 0;
    else if (playerPos.getX() > cells[0].length - 5) x = cells.length - 10;
    else x = (int) (playerPos.getX() - 4); //dont understand why this is 5 (see next note)

    if (playerPos.getY() < 5) y = 0;
    else if (playerPos.getY() > cells.length - 5) y = cells[0].length - 10;
    else y = (int) (playerPos.getY() - 4); //and this is 3... :s

    int tempx = x;
    int tempy = y;

    for (int i = 0; i < 9; i++) {
      tempy = y;
      for (int j = 0; j < 9; j++) {
        if((tempx >= 0 && tempx <= cells.length-1) && (tempy >= 0 && tempy <= cells[0].length -1)) {
          ret[i][j] = cells[tempx][tempy];
        }else{
          ret[i][j] = null;
        }
        tempy++;
      }
      tempx++;
    }
    return ret;
  }

  /**
   * Iterates through a 9x9 grid around the player. Adds them to an appropriate list
   * which is used for drawing in order. Also updates all updatable items
   * (things with animation)
   * @param tuple
   */
  public void update(RenderTuple tuple) {
    player = tuple.getActors()[0];
    Cell[][] surround = getSurround(tuple.getCells(), player);

    playerSprite.update(player.getDirection());
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if(surround[i][j] != null) {
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

  int offset = 0;

  private BufferedImage[] getTransitionImages(String dir){
    BufferedImage[] transitionImages = new BufferedImage[9];

    Cell[][] surround = getSurround(map, player);

    int x = 0;
    int y = 0;
    int i = 0;
    int k = 0;
    int l = 0;

    for(int j = 0; j < 9; j++){
      BufferedImage image = null;
      switch (dir) {
        case "north":
          offset += -4;
          i = 0;
          k = 0;
          l = 64;
          x = surround[i][j].getX();
          y = surround[i][j].getY();
          break;
        case "east":
          offset += 4;
          i = surround[0].length;
          k = 0;
          l = 0;
          x = surround[j][i].getX();
          y = surround[j][i].getY();
          break;
        case "south":
          offset += 4;
          i = surround.length;
          k = 0;
          l = 0;
          x = surround[i][j].getX();
          y = surround[i][j].getY();
          break;
        case "west":
          offset += -4;
          i = 0;
          k = 60;
          l = 0;
          x = surround[j][i].getX();
          y = surround[j][i].getY();
          break;
      }

      Object sprite = sprites[y-1][x-1];
      if(sprite instanceof WallTile){
        WallTile wt = (WallTile)sprite;
        image = wt.getImage();
      }else if(sprite instanceof Door) {
        Door door = (Door) sprite;
        image = door.getImage();
      }else if(sprite instanceof KeyCard) {
        KeyCard key = (KeyCard) sprite;
        image = joinImage(key.getImage());
      }else if(sprite instanceof EnergyBall) {
        EnergyBall eb = (EnergyBall) sprite;
        image = joinImage(eb.getImage());
      }else{
        image = Assets.FLOOR[0][0];
      }
      if(image != null) {
        transitionImages[j] = image.getSubimage(k, l + offset, image.getWidth(), image.getHeight());
      }
    }
    return transitionImages;
  }

  //UNTESTED - joins two images together - so the energy and key can be drawn on top of the floors.
  private BufferedImage joinImage(BufferedImage image){
    BufferedImage target = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) target.getGraphics();
    g2d.drawImage(image, 0, 0, null);
    g2d.drawImage(Assets.FLOOR[0][0], 0, 0, null);
    g2d.dispose();
    return target;
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


    g.drawImage(Assets.MAPBACKGROUND[0][0], 0, 0, this);
    //currently using static images - not retrieved from the object itself yet (it didn't work when I tried)
    for(int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {

        //THIS SHIT IT TOO HARD. WILL WORRY ABOUT TRANSITION LATER
//        if(player != null && player.getIsMoving()) {
//          String playerDir = player.getDirection();
//          BufferedImage[] transition = getTransitionImages(playerDir);
//          //if player is moving, render the screen
//          switch (playerDir) {
//            case "up":
//              y += -4;
//              g.drawImage(transition[j], 0, j* y, this);
//              break;
//            case "right":
//              x += 4;
//              g.drawImage(transition[i], 0, 64 * i + 4, this);
//              break;
//            case "down":
//              y += -4;
//              g.drawImage((transition[j]), 64 * j, 576 + y, null);
//              break;
//            case "left":
//              x += -4;
//              g.drawImage(transition[i], 576 + x, 64 * j, null);
//              break;
//          }
//        }

        if(info != null && i == info.getX() && j == info.getY()){
          g.drawImage(info.getImage(), y * i, x * j, this);
        }
        if (floor[i][j] != null) {
          g.drawImage(Assets.FLOOR[0][0], i * y, x * j, this);
        }
        if (wall[i][j] != null) {
          g.drawImage(wallObjects.get(wall[i][j]).getImage(), y * i, x * j, this);
        }
        if (door[i][j] != null) {
          g.drawImage(Assets.DOOR[0][0], y * i, x * j, this);
        }
        if (energy[i][j] != null) {
          g.drawImage(energyObjects.get(energy[i][j]).getImage(), y * i, x * j, this);
        }
        if (key[i][j] != null) {
          g.drawImage(keyObjects.get(key[i][j]).getImage(), y * i, x * j, this);
        }
      }
    }
    if(playerSprite != null) g.drawImage(playerSprite.getImage(), 4 * 64, 4 * 64, this);

    //clears lists for next frame
    clearLists();

  }

}