package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.maze.cells.*;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;
import nz.ac.vuw.ecs.swen225.gp20.render.sprites.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Allows the game map to be rendered and animated in a 9 x 9 grid around the player.
 * This class initialises all render-able objects and updates them (if they need to be animated) every tick.
 *
 * @author Owen Nicholson 300130653
 */
public class MapPane {

  private Assets assets;

  BufferedImage bg;
  BufferedImage infoAsset;
  BufferedImage floorAsset;

  //Lists to be populated with Cells surrounding player in 11x11 grid
  private Cell[][] floor = new Cell[11][11];
  private Cell[][] wall = new Cell[11][11];
  private Cell[][] hole = new Cell[11][11];
  private Cell[][] door = new Cell[11][11];
  private Cell[][] energy = new Cell[11][11];
  private Cell[][] key = new Cell[11][11];
  private Cell[][] exit = new Cell[11][11];
  private Cell[][] exitLock = new Cell[11][11];
  private Cell[][] info = new Cell[11][11];

  //  private HostileMob[] hostileMobs;
  private ActorSprite[][] visibleMobs = new ActorSprite[11][11];

  private ActorSprite[] mobs;
  private ActorSprite[][] mobsOnMap;


  private ActorSprite playerSprite;
//  private ArrayList<ActorSprite> hostileSprites;
//  private ArrayList<ActorSprite> pushable;

  private Object[][] sprites;

  // HashMaps to store all rendered objects in the game
  private HashMap<Cell, WallTile> wallObjects = new HashMap<>();
  private HashMap<Cell, HoleTile> holeObjects = new HashMap<>();
  private HashMap<Cell, Door> doorObjects = new HashMap<>();
  private HashMap<Cell, EnergyBall> energyObjects = new HashMap<>();
  private HashMap<Cell, KeyCard> keyObjects = new HashMap<>();


  //Single Rendered object (only one per map)
  private Info infoOb;
  private ExitLock exitLockOb;
  private Exit exitOb;


//  private Actor player;

  //transition offset
  int offsetX = 0;
  int offsetY = 0;


  public MapPane(Assets assets) {
    this.assets = assets;
    init();
  }

  private void init() {
    this.bg = assets.getAsset("mapBackground")[0][0];
    this.floorAsset = assets.getAsset("floor")[0][0];
    this.infoAsset = assets.getAsset("info")[0][0];
  }

  /**
   * Initialises all 'sprites' in game, based on the position of the cells.
   * Makes it easy to locate each render object and to avoid assigning them to the cells themselves.
   *
   * @param cells  The tiles of the game map.
   * @param actors Array including the player and all other mobs in game (hostile/neutral)
   */
  public void initAnimationObjects(Cell[][] cells, Actor[] actors) {

    mobsOnMap = new ActorSprite[cells.length][cells[0].length];

    this.playerSprite = new ActorSprite(actors[actors.length-1], assets);

    //creates array of actor array - player
    mobs = new ActorSprite[actors.length - 1];
    //populates array with all other mobs
    for (int m = 0; m < actors.length-1; m++) {
      ActorSprite mob = new ActorSprite(actors[m], assets);
      mobsOnMap[mob.getX()][mob.getY()] = mob;
      mobs[m] = mob;
    }

    sprites = new Object[cells.length][cells[0].length];

    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        if (cells[i][j] != null) {
          switch (cells[i][j].getName()) {
            case "wall":
              WallTile wt = new WallTile(assets);
              Cell[] neighbours = getWallNeighbours(cells, j, i);
              wt.setWallType(neighbours[0], neighbours[1], neighbours[2], neighbours[3]); //wall object initialised with cells at each cardinal direction
              sprites[i][j] = wt;
              wallObjects.put(cells[i][j], wt);
              break;
            case "water":
              HoleTile ht = new HoleTile(assets);
              Cell[] holeNeighbours = getWallNeighbours(cells, j, i);
              ht.setWallType(holeNeighbours[0], holeNeighbours[1], holeNeighbours[2], holeNeighbours[3]);
              sprites[i][j] = ht;
              holeObjects.put(cells[i][j], ht);
              break;
            case "treasure":
              EnergyBall eb = new EnergyBall(assets);
              sprites[i][j] = eb;
              energyObjects.put(cells[i][j], eb);
              break;
            case "key":
              KeyCard kc = new KeyCard(assets, cells[i][j].getColor());
              sprites[i][j] = kc;
              keyObjects.put(cells[i][j], kc);
              break;
            case "door":
              Door door;
              if (i > 0 && (cells[i][j + 1].getName().equals("wall") || cells[i][j - 1].getName().equals("wall"))) {
                door = new Door(assets, cells[i][j].getColor(), true, true);
              } else {
                door = new Door(assets, cells[i][j].getColor(), false, true);
              }
              sprites[i][j] = door;
              doorObjects.put(cells[i][j], door);
              break;
            case "exit":
              Exit e = new Exit(assets, i, j);
              sprites[i][j] = e;
              exitOb = e;
              break;
            case "exit lock":
              ExitLock el = new ExitLock(assets, i, j);
              sprites[i][j] = el;
              exitLockOb = el;
              break;
            case "info":
              infoOb = new Info(assets, i, j, infoAsset);
              break;

            default:
              break;

          }
        }
      }
    }
  }

  /**
   * Finds the neighbouring cells of a wall (all cardinal directions).
   *
   * @param cells All map cells.
   * @param x     the x coordinate of the main cell being checked.
   * @param y     the y coordinate of the main cell being checked.
   * @return A cell array containing between 1 and 4 cells which are adjacent to the main cell in north, east, south, west order..
   */
  private Cell[] getWallNeighbours(Cell[][] cells, int x, int y) {
    Cell[] neighbours = new Cell[4];
    int lengthX = cells[0].length;
    int lengthY = cells.length;

    if (x != 0) {
      neighbours[1] = cells[y][x - 1];
      if (x < lengthX - 1) neighbours[3] = cells[y][x + 1];
      else neighbours[3] = null;
    } else {
      neighbours[1] = null;
      neighbours[3] = cells[y][x + 1];
    }
    if (y != 0) {
      neighbours[0] = cells[y - 1][x];
      if (y < lengthY - 1) neighbours[2] = cells[y + 1][x];
      else neighbours[2] = null;
    } else {
      neighbours[0] = null;
      neighbours[2] = cells[y + 1][x];
    }

    return neighbours;
  }

  /**
   * Gets an 11x11 grid around the player. Will keep the player centered on the screen.
   *
   * @param cells  All cells for entire maze
   * @param player The player's character - for obtaining current position
   * @return 9x9 Cell array
   */
  private Cell[][] getSurround(Cell[][] cells, ActorSprite player) {
    Cell[][] ret = new Cell[11][11]; //a new array to store all the cells around the player
    Point playerPos = new Point(player.getX(), player.getY());

    Point botRight = new Point(player.getX() + 5, player.getY() + 5);

    int x = (int) playerPos.getX() - 5;

    for (int i = 0; i < 11; i++) {
      int y = (int) playerPos.getY() - 5;
      for (int j = 0; j < 11; j++) {
//        for (ActorSprite actor : mobs) {
//          if (actor.getX() == botRight.getX() - (botRight.getX() - i) && actor.getY() == botRight.getY() - (botRight.getY() - j)) {
//            visibleMobs[i][j] = actor;
//          }
//        }
        if ((x >= 0 && x < cells.length) && (y >= 0 && y < cells[0].length)) {
          visibleMobs[i][j] = mobsOnMap[x][y];
        } else {
          visibleMobs[i][j] = null;
        }

        if ((x >= 0 && x < cells.length) && (y >= 0 && y < cells[0].length)) {
          ret[i][j] = cells[x][y];
        } else {
          ret[i][j] = null;
        }
        y++;
      }
      x++;
    }
    return ret;
  }

  /**
   * Iterates through an 11x11 grid around the player. Adds them to an appropriate list
   * which is used for drawing in order. Also updates all updatable items
   * (things with animation)
   *
   * @param tuple tuple containing Cells and actors
   */
  public void update(RenderTuple tuple) {
    //update player with direction
//    player = tuple.getActors()[0];
    playerSprite.update();

    //TODO: add player animation - might need to combine mobs and player together.

    //update all mobs with direction (this can probably be condensed as all actors)
    Cell[][] surround = getSurround(tuple.getCells(), playerSprite);


    for (int i = 0; i < 11; i++) {
      for (int j = 0; j < 11; j++) {
        if (surround[i][j] != null) {
          switch (surround[i][j].getName()) {
            case "free":
              floor[i][j] = surround[i][j];
              break;
            case "wall":
              wall[i][j] = surround[i][j];
              break;
            case "water":
              hole[i][j] = surround[i][j];
              break;
            case "door":
              door[i][j] = surround[i][j];
              if (doorObjects.containsKey(surround[i][j])) {
                if(!surround[i][j].getIsSolid()) {
                  doorObjects.get(surround[i][j]).setIsSolid(false);
                }
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
              info[i][j] = surround[i][j];
              break;
            case "exit":
              exit[i][j] = surround[i][j];
              if (exitOb != null) {
                exitOb.update();
              }
              break;
            case "exit lock":
              exitLock[i][j] = surround[i][j];
              if (exitLockOb != null) {
                exitLockOb.update();
              }
              break;
            default:
              break;
          }
        }
      }
    }
    for (ActorSprite[] actorSprites : mobsOnMap) {
      for (ActorSprite actorSprite : actorSprites) {
        if (actorSprite != null) {
          actorSprite.update();
        }
      }
    }
//    repaint();
  }

  /**
   * Clears all lists ready for next frame
   */
  private void clearLists() {
    floor = new Cell[11][11];
    wall = new Cell[11][11];
    hole = new Cell[11][11];
    door = new Cell[11][11];
    energy = new Cell[11][11];
    key = new Cell[11][11];
    exit = new Cell[11][11];
    exitLock = new Cell[11][11];
    info = new Cell[11][11];

  }


  /**
   * Draws all visible sprites(in the 9x9 grid around player) in order from the floor up.
   * The extra lines are for transition frames  - they are drawn off-screen and transition in
   * as the player moves.
   *
   * @param g graphics object
   */
//  @Override
  public void draw(Graphics g) {
//    super.paintComponent(g);


    // transition animation - draws all objects depending on offset (speed)
    if (playerSprite != null && playerSprite.getIsMoving()) {

      int speed = 12;
      switch (playerSprite.getDirection()) {
        case UP:
          offsetY += speed;
          break;
        case DOWN:
          offsetY -= speed;
          break;
        case LEFT:
          offsetX += speed;
          break;
        case RIGHT:
          offsetX -= speed;
          break;

        default:
          break;
      }
    } else {
      offsetX = offsetY = 0;
    }

    int x = 64;
    int y = 64;

    g.drawImage(bg, 0, 0, null);
    for (int i = 0; i < 11; i++) {
      for (int j = 0; j < 11; j++) {

        if (floor[i][j] != null) {
          g.drawImage(floorAsset, x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (wall[i][j] != null && wallObjects.size() > 0) {
          g.drawImage(wallObjects.get(wall[i][j]).getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (hole[i][j] != null && holeObjects.size() > 0) {
          g.drawImage(holeObjects.get(hole[i][j]).getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (door[i][j] != null && doorObjects.size() > 0) {
          if (doorObjects.get(door[i][j]).isVertical()) {
            g.drawImage(doorObjects.get(door[i][j]).getImage(), x * i + offsetX - x, y * j + offsetY - y - 42, null);
          } else {
            g.drawImage(doorObjects.get(door[i][j]).getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
          }
        }
        if (energy[i][j] != null && energyObjects.size() > 0) {
          g.drawImage(energyObjects.get(energy[i][j]).getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (key[i][j] != null && keyObjects.size() > 0) {
          g.drawImage(keyObjects.get(key[i][j]).getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (exit[i][j] != null && exitOb != null) {
          g.drawImage(exitOb.getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (info[i][j] != null && infoOb != null) {
          g.drawImage(infoOb.getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }
        if (exitLock[i][j] != null && exitLockOb != null) {
          g.drawImage(exitLockOb.getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }

        if (visibleMobs[i][j] != null) {
          g.drawImage(visibleMobs[i][j].getImage(), x * i + offsetX - x, y * j + offsetY - y, null);
        }

      }
    }


    //draws player last to remain on top (uses abolsute positioning.... for now)
    if (playerSprite != null) g.drawImage(playerSprite.getImage(), 4 * 64, 4 * 64, null);

    //clears lists for next frame
    clearLists();

  }

}