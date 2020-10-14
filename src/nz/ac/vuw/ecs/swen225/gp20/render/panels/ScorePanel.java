package nz.ac.vuw.ecs.swen225.gp20.render.panels;

import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Allows the current status of the game to be displayed - this includes the Level information,
 * timer, and inventory.
 *
 * @author Owen N
 *
 * TODO: Include a space for the 'treasure' items remaining
 */
public class ScorePanel extends JPanel {


  private Image background;
  private BufferedImage[] digits;
  char[] chars = {0, 0, 0};
  BufferedImage[][] inventorySprites;
  private HashMap<String, Integer> inventory;

  BufferedImage[] info;

  boolean onInfo;

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
  public void update(int timeLimit, RenderTuple tuple){
    String time = String.valueOf(timeLimit);
    chars = time.toCharArray();
    this.inventory = tuple.getInventory();
    this.info = drawString(tuple.getInfo());
    this.onInfo = tuple.isPlayerOnInfo();
    repaint();
  }

  /**
   * Constructs an array of images based on the input
   * @param s A string to be converted
   * @return a BufferedImage array of the string converted to images (custom font)
   */
  private BufferedImage[] drawString(String s){
    if(s == null) return null;
    BufferedImage[] string = new BufferedImage[s.length()];

    for(int i = 0; i < s.length(); i++){
      char ch = s.charAt(i);
      int c = 0;
      if(ch >= 65 && ch <= 90) {
        string[i] = Assets.FONT[0][ch - 65];
      }
      if(ch >= 97 && ch <= 122){
        string[i] = Assets.FONT[1][ch - 97];
      }
    }
    return string;
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
    //TODO: Add other inventory items.
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
        g.drawImage(inventorySprites[i][j], 92 + (countX * 32) + 3, 437 + (countY * 32) + 3, this);
        g.drawImage(inventorySprites[3][c-1], 92 + (countX * 32) + 3, 437 + (countY * 32) + 3, this);
        countX++;
        if (countX == 4) {
          countY++;
          countX = 0;
        }
      }

    }
    //can only handle a single line up to 22 characters (including spaces)
    if(onInfo && info != null){
      for(int f = 0; f < info.length; f++){
        g.drawImage(info[f], 57 + (f * 9), 259, this);
      }
    }



  }

}
