package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GamePanel extends JPanel {

  //  Assets assets = new Assets();

  EnergyBall eb;
  KeyCard kcg;

  private ArrayList<FloorTile> floorTiles = new ArrayList<>();
  private ArrayList<WallTile> wallTiles = new ArrayList<>();
  private ArrayList<Door> doors = new ArrayList<>();
  private ArrayList<EnergyBall> energyBalls = new ArrayList<>();
  private ArrayList<Key> keys = new ArrayList<>();

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
    eb = new EnergyBall();
    kcg = new KeyCard();
    Graphics g = image.getGraphics();
  }

  //needs to be called when the map has been loaded and all objects have been initialised.
  //this assigns animation objects to all objects that can animate so they can be updated.
  public void initAnimationObjects(Cell[][] cells){
    for(int i = 0; i < cells.length; i++){
      for(int j = 0; j < cells[i].length; i++){
        String[] data = cells[i][j].getRenderData().split(":");
        if(Integer.parseInt(data[1]) != 0){
          switch(data[0]){
            case "energy_ball":
              cells[i][j].setAnimationObject(new EnergyBall());
            case "keycard_green":
              cells[i][j].setAnimationObject(new KeyCard());
            case "door":
              cells[i][j].setAnimationObject(new Door());
          }
        }
      }
    }
  }

  /**
   * iterates through a 9x9 grid around the player. Will
   * @param cells
   * @param actors
   */
  public void update(Cell[][] cells, Actor[] actors) {

    //get player position
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
        String data[] = cells[i][j].getRenderData().split(":");
        if (data[0].equals("wall")) {
          wallTiles.add
          int animated = Integer.parseInt(data[0]);

          if (animated != 0) {
            cells[i][j].getAnimationObject().update();
          }
        }
      }
    /*TODO: Update all objects in game - iterate through the tiles around player.
            Update all doors (if closed, won't update, if open, updating it's key frame).
            Update all animated objects such as Balls, Keys etc.
            Update player depending on facing direction and if moving or not.
     */

      eb.update();
      kcg.update();

      // TODO repaint();
    }

  }

  /**
   * Test method for animations - based of system time. can swap out when tick system is implemented (runs at 30FPS)
   */
  public void run() {
    while(true) {
      Cell[][] cell = new Cell[0][0];
      this.update(cell);
      repaint();
      long start = System.currentTimeMillis();
      while (true) {
        long delay = 33;
        if (System.currentTimeMillis() >= start + delay) {
          break;
        }

      }
    }
  }



  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // draw each set of images in turn
    // Tiles -> Doors -> Balls/Key/Items -> Player

    g.drawImage(eb.getImage(), 0, 0, this);
    g.drawImage(kcg.getImage(), 64, 0, this);

    //get player position
    Point playerPos = new Point(actors[0].getX, actors.getY);


    int x;
    int y;

    if(playerPos.getX() <= 4) x = 0;
    else if(playerPos.getX() >= cells.length -4) x = cells.length - 9;
    else x = (int) (playerPos.getX() - 4);

    if(playerPos.getY() <= 4) y = 0;
    else if(playerPos.getY() >= cells[0].length -4) y = cells[0].length - 9;
    else y = (int) (playerPos.getX() - 4);

    for(int i = x; i < x + 9; i++){
      for(int j = y; j < y + 9; j++){
        }
      }
    }


  }

}