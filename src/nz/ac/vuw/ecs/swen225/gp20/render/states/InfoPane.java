package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows the current status of the game to be displayed - this includes the Level information,
 * timer, and inventory. Although being drawn at the same time as the Map Pane, separated
 * to make it easier to work on.
 *
 * @author Owen Nicholson 300130653
 */
public class InfoPane {

  Assets assets;

  private Image bg;
  private BufferedImage[] digits;
  private char[] chars = {0, 0, 0}; //array of numbers to print out - represents time remaining
  private BufferedImage[][] inventorySprites;
  private HashMap<String, Integer> inventory;

  private BufferedImage[] info; //array of font images to print out on the information read-out

  boolean onInfo; //true if the player is on an info tile

  private BufferedImage[][] font;

  private int total; //total amount of energy
  private int gathered; //energy picked up
  private int energyFilled; //amount of energy filled (an x coordinate to draw the energy bar)
  private BufferedImage energyBarShade;
  private BufferedImage energyBar;

  int gameSize = 576; // the width of the MapPane image (to draw this pane in the correct place)

  public InfoPane(Assets assets) {
    this.assets = assets;
    init();
  }

  /**
   * Initialises the background - more coming soon...
   */
  private void init() {
    this.bg = assets.getAsset("infoBackground")[0][0];
    this.inventorySprites = assets.getAsset("inventory");
    this.digits = assets.getAsset("digits")[0];
    this.font = assets.getAsset("font");
    this.energyBarShade = assets.getAsset("energyBarShade")[0][0];
    this.energyBar = assets.getAsset("energyBar")[0][0];
  }


  /**
   * Gets a time and converts to char array to be converted into an int
   *
   * @param timeLimit the time in 'int'
   */
  public void update(int timeLimit, RenderTuple tuple) {
    String time = String.valueOf(timeLimit);
    chars = time.toCharArray();
    this.inventory = tuple.getInventory();
    this.info = drawString(tuple.getInfo());
    this.onInfo = tuple.isPlayerOnInfo();
    this.gathered = tuple.getTreasureCollected();
    this.total = gathered + tuple.getTreasureLeft();
  }

  /**
   * Constructs an array of images based on the input.
   *
   * @param s A string to be converted.
   * @return a BufferedImage array of the string converted to images (custom font).
   */
  private BufferedImage[] drawString(String s) {
    if (s == null) return null;
    BufferedImage[] string = new BufferedImage[s.length()];

    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);

      if (ch >= 65 && ch <= 90) {
        string[i] = font[0][ch - 65];
      }
      if (ch >= 97 && ch <= 122) {
        string[i] = font[1][ch - 97];
      }
    }
    return string;
  }

  /**
   * Gets a sub-image of the energybar that is greyed out. The greyed out image
   * represents the percentage of energy not collected.
   * Sets 'energyFilled' which represents the position to draw the greyed out sub-image.
   *
   * @return The image representing the percentage of energy remaining.
   */
  private BufferedImage getEnergyLevel() {
    int length = energyBarShade.getWidth();
    energyFilled = (int) (length * (gathered / (double) total));
    return energyBarShade.getSubimage(energyFilled, 0, energyBarShade.getWidth() - energyFilled, energyBarShade.getHeight());
  }


  /**
   * Converts the char array to numeric values which are then drawn on screen
   * with absolute values (dictated by the background graphic of the ScorePanel.
   *
   * @param g Graphics object
   */
//  @Override
  public void draw(Graphics g) {
//    super.paintComponent(g);
    g.drawImage(bg, gameSize, 0, null);

    g.drawImage(energyBar, gameSize + 74, 330, null);
    if (energyBarShade != null) {
      if (total != gathered) {
        g.drawImage(getEnergyLevel(), gameSize + 74 + energyFilled, 330, null);
      }
    }


    int offset = 0;
    if (chars.length == 2) {
      offset = 1;
      g.drawImage(digits[0], gameSize + 110, 133, null);
    } else if (chars.length == 1) {
      offset = 2;
      g.drawImage(digits[0], gameSize + 110, 133, null);
      g.drawImage(digits[0], gameSize + 142, 133, null);
    }

    for (int i = 0; i < chars.length; i++) {
      int digit = Character.getNumericValue(chars[i]); // converts char to the interger value.
      if (digit >= 0) { //to avoid null pointer
        g.drawImage(digits[digit], gameSize + 110 + ((i + offset) * 32), 133, null);
      }

    }

    // INVENTORY RENDERER
    // renders each inventory item.
    //TODO: Add other inventory items.
    int countX = 0; //columns of the inventory panel
    int countY = 0; //for each row of the inventory panel
    int j = 0; //position of the colored key in the array
    int c = 0; //the count of the inventory item
    if (inventory != null) {
      for (Map.Entry<String, Integer> s : inventory.entrySet()) {
        switch (s.getKey()) {
          case "redkey":
            j = 0;
            c = s.getValue();
            break;
          case "greenkey":
            j = 1;
            c = s.getValue();
            break;
          case "bluekey":
            j = 2;
            c = s.getValue();
            break;
          case "yellowkey":
            j = 3;
            c = s.getValue();
            break;
          default:
            break;
        }
        g.drawImage(inventorySprites[0][j], gameSize + 92 + (countX * 32) + 3, 437 + (countY * 32) + 3, null);
        g.drawImage(inventorySprites[3][c - 1], gameSize + 92 + (countX * 32) + 3, 437 + (countY * 32) + 3, null);
        countX++;
        if (countX == 4) {
          countY++;
          countX = 0;
        }
      }

    }
    //can only handle a single line up to 22 characters (including spaces)
    if (onInfo && info != null) {
      for (int f = 0; f < info.length; f++) {
        g.drawImage(info[f], gameSize + 57 + (f * 9), 259, null);
      }
    }


  }

}
