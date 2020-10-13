package nz.ac.vuw.ecs.swen225.gp20.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

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
  public static BufferedImage[][]           FLOOR = loadGif(path, "floortile.gif", 64, 64);
  public static BufferedImage[][]            WALL = loadGif(path, "walltiles.gif", 64, 64);
  public static BufferedImage[][]            DOOR = loadGif(path, "doorsheet.gif", 64, 64);
  public static BufferedImage[][]           VDOOR = loadGif(path, "vertdoorsheet.gif", 64, 106);
  public static BufferedImage[][]      ENERGYBALL = loadGif(path, "energysprite.gif", 64, 64);
  public static BufferedImage[][]       KEYCARD_G = loadGif(path, "keycardsprites.gif", 64, 64);
  public static BufferedImage[][]            INFO = loadGif(path, "info.gif", 64, 64);
  public static BufferedImage[][]   MAPBACKGROUND = loadGif(path, "mapbackground.gif", 576, 576);
  public static BufferedImage[][]        EXITLOCK = loadGif(path, "exitlock.gif", 64, 64);
  public static BufferedImage[][]            EXIT = loadGif(path, "exit.gif", 64, 64);

  //Score/info gifs
  public static BufferedImage[][] SCOREBACKGROUND = loadGif(path, "scorepanelbackground2.gif", 300, 576);
  public static BufferedImage[][]          DIGITS = loadGif(path, "digits.gif", 32, 45);
  public static BufferedImage[][]       MENUBARBG = loadGif(path, "menubarbg.gif", 876, 28);
  public static BufferedImage[][]       INVENTORY = loadGif(path, "inventorysheet.gif", 32, 32);

  //load player spritesup
  public static BufferedImage[][]          PLAYER = loadGif(path, "player.gif", 64, 64);


//  public static BufferedImage[][]           PAUSE = loadGif(path, "pause.gif", 876, 576);
//  public static BufferedImage[][]    PAUSEPRESSED = loadGif(path, "pause_depressed.gif", 102, 22);




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

}
