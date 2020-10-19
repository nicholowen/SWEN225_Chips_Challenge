package nz.ac.vuw.ecs.swen225.gp20.render.managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Loads assets into 2D BufferedImage Arrays. This allows multiple states
 * of image to be loaded from a single sprite sheet.
 *
 * Credit: Zequnyu - https://github.com/zequnyu
 * @author Owen N
 */
public class Assets
{

  private static File path = new File("resources/Assets/Images/");

  //Load gifs TODO: add floor tiles and door tiles/animations
  //Map/Game gifs
  private static BufferedImage[][]             FLOOR;
  private static BufferedImage[][]              WALL;
  private static BufferedImage[][]              DOOR;
  private static BufferedImage[][]              HOLE;
  private static BufferedImage[][]             VDOOR;
  private static BufferedImage[][]        ENERGYBALL;
  private static BufferedImage[][]         KEYCARD_G;
  private static BufferedImage[][]              INFO;
  private static BufferedImage[][]          EXITLOCK;
  private static BufferedImage[][]              EXIT;

  //Score/info gifs
  private static BufferedImage[][]             DIGITS;
  private static BufferedImage[][]          INVENTORY;
  private static BufferedImage[][]               FONT;
  //energybar
  private static BufferedImage[][]     ENERGYBARSHADE;
  private static BufferedImage[][]          ENERGYBAR;

  //mob sprites
  private static BufferedImage[][]             PLAYER;
  private static BufferedImage[][]         HOSTILEMOB;

  //panel backgrounds
  private static BufferedImage[][]               LOGO;
  private static BufferedImage[][]      MAPBACKGROUND;
  private static BufferedImage[][]    SCOREBACKGROUND;
  private static BufferedImage[][]               MENU;
  private static BufferedImage[][]              LEVEL;
  private static BufferedImage[][]              PAUSE;

  //State Buttons
  private static BufferedImage[][]        MENUBUTTONS;
  private static BufferedImage[][]       PAUSEBUTTONS;
  private static BufferedImage[][] LEVELSELECTBUTTONS;

  public Assets(){
    init();
  }

  private void init(){

    FLOOR = loadGif(path, "floortile.gif", 64, 64);
    WALL = loadGif(path, "walltiles.gif", 64, 64);
    DOOR = loadGif(path, "doorsheet.gif", 64, 64);
    HOLE = loadGif(path, "hole.gif", 64, 64);
    VDOOR = loadGif(path, "vertdoorsheet.gif", 64, 106);
    ENERGYBALL = loadGif(path, "energysprite.gif", 64, 64);
    KEYCARD_G = loadGif(path, "keycardsprites.gif", 64, 64);
    INFO = loadGif(path, "info.gif", 64, 64);
    EXITLOCK = loadGif(path, "exitlock.gif", 64, 64);
    EXIT = loadGif(path, "exit.gif", 64, 64);
    DIGITS = loadGif(path, "digits.gif", 32, 45);
    INVENTORY = loadGif(path, "inventorysheet.gif", 32, 32);
    FONT = loadGif(path, "font.gif", 9, 12);
    ENERGYBARSHADE = loadGif(path, "energy_top.gif", 152, 22);
    ENERGYBAR = loadGif(path, "energy_bot.gif", 152, 22);
    PLAYER = loadGif(path, "player.gif", 64, 64);
    HOSTILEMOB = loadGif(path, "hostile_mob.gif", 64, 64);
    LOGO = loadGif(path, "logo.gif", 892, 576);
    MAPBACKGROUND = loadGif(path, "mapbackground.gif", 576, 576);
    SCOREBACKGROUND = loadGif(path, "scorepanelbackground.gif", 300, 576);
    MENU = loadGif(path, "menu.gif", 892, 576);
    LEVEL = loadGif(path, "levels.gif", 892, 576);
    PAUSE = loadGif(path, "pause.gif", 892, 576);
    MENUBUTTONS = loadGif(path, "menuButtons.gif", 159, 51);
    PAUSEBUTTONS = loadGif(path, "pauseButtons.gif", 159, 51);
    LEVELSELECTBUTTONS = loadGif(path, "levelSelectButtons.gif", 159, 51);

  }




  /**
   * Gets a sprite sheet from the resources folder and splits them up into individual images.
   * @param path directory path
   * @param f image file name
   * @param w width of each image (px)
   * @param h height of each image (px)
   * @return 2D Image array - allows for allows for multiple state animation frames for single object - ie player up, down, left right etc.
   */
  public static BufferedImage[][] loadGif(File path, String f, int w, int h) {
    BufferedImage[][] gif;
    try{
      BufferedImage spriteSheet = ImageIO.read(new File(path, f));

      //Divides sprite sheet into individual images.
      int width = spriteSheet.getWidth() / w;
      int height = spriteSheet.getHeight() / h;
      gif = new BufferedImage[height][width];
      for(int i = 0; i < height; i++){
        for(int j = 0; j < width; j++){
          System.out.println(i + "" + j);
          gif[i][j] = spriteSheet.getSubimage(w * j, h * i, w, h); //adds portion of the image 'w x h'
        }
      }

      return gif;

    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("Error loading graphics.");
      System.exit(0);
    }
    return null;
  }

  public BufferedImage[][] getAsset(String asset) {
    BufferedImage[][] ret;
    switch(asset){
      case "floor":
        ret =  FLOOR;
        break;
      case "wall":
        ret =  WALL;
        break;
      case "hole":
        ret =  HOLE;
        break;
      case "door":
        ret =  DOOR;
        break;
      case "vdoor":
        ret =  VDOOR;
        break;
      case "energy":
        ret =  ENERGYBALL;
        break;
      case "keycard":
        ret =  KEYCARD_G;
        break;
      case "info":
        ret =  INFO;
        break;
      case "exitLock":
        ret =  EXITLOCK;
        break;
      case "exit":
        ret =  EXIT;
        break;
      case "digits":
        ret =  DIGITS;
        break;
      case "inventory":
        ret =  INVENTORY;
        break;
      case "font":
        ret =  FONT;
        break;
      case "energyBarShade":
        ret =  ENERGYBARSHADE;
        break;
      case "energyBar":
        ret =  ENERGYBAR;
        break;
      case "player":
        ret =  PLAYER;
        break;
      case "hostileMob":
        ret =  HOSTILEMOB;
        break;
      case "logo":
        ret =  LOGO;
        break;
      case "mapBackground":
        ret =  MAPBACKGROUND;
        break;
      case "infoBackground":
        ret =  SCOREBACKGROUND;
        break;
      case "menuBackground":
        ret =  MENU;
        break;
      case "levelSelectBackground":
        ret =  LEVEL;
        break;
      case "pauseBackground":
        ret =  PAUSE;
        break;
      case "levelSelectButtons":
        ret =  LEVELSELECTBUTTONS;
        break;
      case "menuButtons":
        ret =  MENUBUTTONS;
        break;
      case "pauseButtons":
        ret =  PAUSEBUTTONS;
        break;
      default:
        return null;
    }
    return ret.clone();
  }
}
