package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Allows the current status of the game to be displayed - this includes the Level information,
 * timer, and inventory.
 *
 * @author Owen N
 *
 * TODO: Include a space for the 'treasure' items remaining, as well as
 *  implement all other information graphics (including custom font for information panel).
 */
public class ScorePanel extends JPanel {

  /*===============================================================
   * PLACE HOLDER - will show the score board, time, and inventory
   *===============================================================
   * contains some garbage code to draw (can see what I am doing)
   */

  private Image background;
  private BufferedImage[] digits;
  char[] chars = {0, 0, 0};
  BufferedImage[][] inventorySprites;
  private HashMap<String, Integer> inventory;

  String button;

  public ScorePanel() {
    setPreferredSize(new Dimension(300, 576));
    this.background = loadBackground();
    requestFocus();
    inventorySprites = Assets.INVENTORY;
    init();
  }

  /**
   * Initialises the background - more coming soon...
   */
  private void init(){
    digits = Assets.DIGITS[0];
  }

  private BufferedImage loadBackground(){
    return Assets.SCOREBACKGROUND[0][0];
  }

  /**
   * Gets a time and converts to char array to be converted into an int
   * @param timeLimit the time in 'int'
   */
  public void update(int timeLimit, HashMap<String, Integer> inventory/*, String button*/){
    String time = String.valueOf(timeLimit);
    chars = time.toCharArray();
    this.inventory = inventory;
    this.button = button;
    repaint();
  }


  /**
   * Converts the char array to numeric values which are then drawn on screen
   * with absolute values (dictated by the background graphic of the ScorePanel
   * @param g Graphics object
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(background, 0, 0, this);

    int offset = 0;
    if (chars.length == 2) {
      offset = 1;
      g.drawImage(digits[0], 110, 133, this);
    } else if (chars.length == 1) {
      offset = 2;
      g.drawImage(digits[0], 110, 133, this);
      g.drawImage(digits[0], 142, 133, this);
    }

    for (int i = 0; i < chars.length; i++) {
      int digit = Character.getNumericValue(chars[i]); // converts char to the interger value.
      if (digit >= 0) { //to avoid null pointer
        g.drawImage(digits[digit], 110 + ((i + offset) * 32), 133, this);
      }

    }

    // INVENTORY RENDERER
    // renders each inventory item.
    int countX = 0; //columns of the inventory panel
    int countY = 0; //for the each row of the inventory panel
    int i = 0;
    int j = 0;
    int c = 0;
    if (inventory != null) {
      for (String s : inventory.keySet()) {
        switch (s) {
          case "redkey":
            j = 0;
            c = inventory.get(s);
            break;
          case "greenkey":
            j = 1;
            c = inventory.get(s);
            break;
          case "bluekey":
            j = 2;
            c = inventory.get(s);
            break;
          case "yellowkey":
            j = 3;
            c = inventory.get(s);
            break;
        }
        // TODO: Draw counter onto inventory slot.
        g.drawImage(inventorySprites[i][j], 92 + (countX * 32) + 3, 471 + (countY * 32) + 3, this);
        g.drawImage(inventorySprites[3][c-1], 92 + (countX * 32) + 3, 471 + (countY * 32) + 3, this);
        countX++;
        if (countX == 4) {
          countY++;
          countX = 0;
        }
      }

    }
//    if(button != null && button.equals("playpause")){
//      g.drawImage(Assets.PAUSEPRESSED[0][0], 96, 539, this);
//    }



  }

}
