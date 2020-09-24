package nz.ac.vuw.ecs.swen225.gp20.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Assets
{
  private static File path = new File("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/");

  //Load gifs TODO: add floor tiles and door tiles/animations
  //public static BufferedImage[][] FLOOR
  public static BufferedImage[][] WALL = loadGif(path, "walltiles.gif", 64, 64);
  //public static BufferedImage[][] DOOR
  public static BufferedImage[][] ENERGYBALL = loadGif(path, "energysprite.gif", 64, 64);
  public static BufferedImage[][] KEYCARD_G = loadGif(path, "key_green.gif", 64, 64);


  //TODO: Player, Key, Door


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
