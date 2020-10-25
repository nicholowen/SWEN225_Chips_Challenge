package nz.ac.vuw.ecs.swen225.gp20.render.managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Loads assets into 2D BufferedImage Arrays. This allows multiple states
 * of image to be loaded from a single sprite sheet.
 * Credit: Zequnyu - https://github.com/zequnyu
 *
 * @author Owen N
 */
public class Assets {

  private static File path = new File("resources/Assets/Images/");

  //Load gifs TODO: add floor tiles and door tiles/animations
  //Map/Game gifs
  private static BufferedImage[][] FLOOR;
  private static BufferedImage[][] GOOP;
  private static BufferedImage[][] WALL;
  private static BufferedImage[][] DOOR;
  private static BufferedImage[][] WATER;
  private static BufferedImage[][] VDOOR;
  private static BufferedImage[][] ENERGYBALL;
  private static BufferedImage[][] KEY;
  private static BufferedImage[][] INFO;
  private static BufferedImage[][] EXITLOCK;
  private static BufferedImage[][] EXIT;

  //mob sprites
  private static BufferedImage[][] PLAYER;
  private static BufferedImage[][] HOSTILEMOB;
  private static BufferedImage[][] HOVERINACTIVE;
  private static BufferedImage[][] HOVERACTIVE;

  //panel backgrounds
  private static BufferedImage[][] LOGO;
  private static BufferedImage[][] MAPBACKGROUND;
  private static BufferedImage[][] SCOREBACKGROUND;
  private static BufferedImage[][] MENU;
  private static BufferedImage[][] LEVEL;
  private static BufferedImage[][] PAUSE;
  private static BufferedImage[][] DEAD;
  private static BufferedImage[][] WIN;
  private static BufferedImage[][] INFORMATION;

  //State Buttons
  private static BufferedImage[][] MENUBUTTONS;
  private static BufferedImage[][] PAUSEBUTTONS;
  private static BufferedImage[][] LEVELSELECTBUTTONS;
  private static BufferedImage[][] DEADBUTTONS;
  private static BufferedImage[][] GAMEOVERBUTTONS;
  private static BufferedImage[][] INFORMATIONBUTTONS;

  //Information panel
  private static BufferedImage[][] DIGITS;
  private static BufferedImage[][] INVENTORY;
  private static BufferedImage[][] FONT;
  private static BufferedImage[][] LEVELFONT;
  private static BufferedImage[][] ENERGYBARSHADE;
  private static BufferedImage[][] ENERGYBAR;
  private static BufferedImage[][] INFOPAUSEBUTTON;
  private static BufferedImage[][] RECORDBUTTON;
  private static BufferedImage[][] CONTROLBUTTONS;
  private static BufferedImage[][] RECORDING;
  private static BufferedImage[][] REPLAYING;


  public Assets() {
    init();
  }

  /**
   * Initialises all assets in game to avoid in-game file loading.
   */
  private void init() {

    //Logo for intro screen
    LOGO = loadGif(path, "logo.gif", 892, 576);

    //Environment
    FLOOR = loadGif(path, "floortile.gif", 64, 64);

    WALL = loadGif(path, "walltiles.gif", 64, 64);
    DOOR = loadGif(path, "doorsheet.gif", 64, 64);
    WATER = loadGif(path, "hole.gif", 64, 64);
    VDOOR = loadGif(path, "vertdoorsheet.gif", 64, 106);
    GOOP = loadGif(path, "goop.gif", 64, 64);
    INFO = loadGif(path, "info.gif", 64, 64);
    EXITLOCK = loadGif(path, "exitlock.gif", 64, 64);
    EXIT = loadGif(path, "exit.gif", 64, 64);

    //Items
    ENERGYBALL = loadGif(path, "energysprite.gif", 64, 64);
    KEY = loadGif(path, "keycardsprites.gif", 64, 64);

    //Actors
    PLAYER = loadGif(path, "playersheet.gif", 64, 64);
    HOSTILEMOB = loadGif(path, "hostilesheet.gif", 64, 64);
    HOVERINACTIVE = loadGif(path, "hover_inactive.gif", 64, 64);
    HOVERACTIVE = loadGif(path, "hover_active.gif", 64, 64);

    //Backgrounds
    MAPBACKGROUND = loadGif(path, "mapbackground.gif", 576, 576);
    SCOREBACKGROUND = loadGif(path, "scorepanelbackground.gif", 300, 576);
    MENU = loadGif(path, "menu.gif", 892, 576);
    LEVEL = loadGif(path, "levelsBackground.gif", 892, 576);
    PAUSE = loadGif(path, "pause.gif", 892, 576);
    DEAD = loadGif(path, "deadBackground.gif", 892, 576);
    WIN = loadGif(path, "winBackground.gif", 892, 576);
    INFORMATION = loadGif(path, "informationBackground.gif", 892, 576);

    //Buttons
    MENUBUTTONS = loadGif(path, "menuButtons.gif", 159, 51);
    PAUSEBUTTONS = loadGif(path, "pauseButtons.gif", 159, 51);
    LEVELSELECTBUTTONS = loadGif(path, "levelSelectButtons.gif", 159, 51);
    DEADBUTTONS = loadGif(path, "deadButtons.gif", 159, 51);
    GAMEOVERBUTTONS = loadGif(path, "winButtons.gif", 159, 51);
    INFOPAUSEBUTTON = loadGif(path, "infoPauseButton.gif", 102, 22);
    RECORDBUTTON = loadGif(path, "recordButton.gif", 26, 38);
    CONTROLBUTTONS = loadGif(path, "controlButtons.gif", 18, 18);
    INFORMATIONBUTTONS = loadGif(path, "informationButtons.gif", 159, 51);

    //In-Game Information
    DIGITS = loadGif(path, "digits.gif", 32, 45);
    INVENTORY = loadGif(path, "inventorysheet.gif", 32, 32);
    FONT = loadGif(path, "font.gif", 9, 12);
    LEVELFONT = loadGif(path, "levelFont.gif", 12, 16);
    ENERGYBARSHADE = loadGif(path, "energy_top.gif", 152, 22);
    ENERGYBAR = loadGif(path, "energy_bot.gif", 152, 22);
    RECORDING = loadGif(path, "recording.gif", 16, 16);
    REPLAYING = loadGif(path, "replaying.gif", 16, 16);

  }


  /**
   * Gets a sprite sheet from the resources folder and splits them up into individual images.
   * Rows are used for different directions or colors.
   * Columns are used for either states or animation frames.
   *
   * @param path directory path
   * @param f    image file name
   * @param w    width of each image (px)
   * @param h    height of each image (px)
   * @return 2D Image array - allows for allows for multiple state animation frames for single object - ie player up, down, left right etc.
   */
  public static BufferedImage[][] loadGif(File path, String f, int w, int h) {
    BufferedImage[][] gif;
    try {
      BufferedImage spriteSheet = ImageIO.read(new File(path, f));

      //Divides sprite sheet into individual images.
      int width = spriteSheet.getWidth() / w;
      int height = spriteSheet.getHeight() / h;
      gif = new BufferedImage[height][width];
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          gif[i][j] = spriteSheet.getSubimage(w * j, h * i, w, h); //adds portion of the image 'w x h'
        }
      }

      return gif;

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error loading graphics.");
      System.exit(0);
    }
    return null;
  }

  /**
   * Gets the image array for the asset, given a description string.
   * @param asset String describing the asset.
   * @return 2D Array of the required asset.
   */
  public BufferedImage[][] getAsset(String asset) {
    BufferedImage[][] ret;
    switch (asset) {
      case "floor": ret = FLOOR;
        break;
      case "goop": ret = GOOP;
        break;
      case "wall": ret = WALL;
        break;
      case "water": ret = WATER;
        break;
      case "door": ret = DOOR;
        break;
      case "vdoor": ret = VDOOR;
        break;
      case "energy": ret = ENERGYBALL;
        break;
      case "key": ret = KEY;
        break;
      case "info": ret = INFO;
        break;
      case "exitLock": ret = EXITLOCK;
        break;
      case "exit": ret = EXIT;
        break;
      case "digits": ret = DIGITS;
        break;
      case "inventory": ret = INVENTORY;
        break;
      case "font": ret = FONT;
        break;
      case "levelFont": ret = LEVELFONT;
        break;
      case "energyBarShade": ret = ENERGYBARSHADE;
        break;
      case "energyBar": ret = ENERGYBAR;
        break;
      case "player": ret = PLAYER;
        break;
      case "hostileMob": ret = HOSTILEMOB;
        break;
      case "dirt": ret = HOVERINACTIVE;
        break;
      case "dirtInactive": ret = HOVERACTIVE;
        break;
      case "logo": ret = LOGO;
        break;
      case "mapBackground": ret = MAPBACKGROUND;
        break;
      case "infoBackground": ret = SCOREBACKGROUND;
        break;
      case "menuBackground": ret = MENU;
        break;
      case "levelSelectBackground": ret = LEVEL;
        break;
      case "pauseBackground": ret = PAUSE;
        break;
      case "levelSelectButtons": ret = LEVELSELECTBUTTONS;
        break;
      case "deadBackground": ret = DEAD;
        break;
      case "winBackground": ret = WIN;
        break;
      case "informationBackground": ret = INFORMATION;
        break;
      case "menuButtons": ret = MENUBUTTONS;
        break;
      case "pauseButtons": ret = PAUSEBUTTONS;
        break;
      case "deadButtons": ret = DEADBUTTONS;
        break;
      case "winButtons": ret = GAMEOVERBUTTONS;
        break;
      case "informationButtons": ret = INFORMATIONBUTTONS;
        break;
      case "infoPauseButton": ret = INFOPAUSEBUTTON;
        break;
      case "recordButton": ret = RECORDBUTTON;
        break;
      case "controlButtons": ret = CONTROLBUTTONS;
        break;
      case "recording": ret = RECORDING;
        break;
      case "replaying": ret = REPLAYING;
        break;
      default:
        return null;
    }
    return ret.clone();
  }
}
