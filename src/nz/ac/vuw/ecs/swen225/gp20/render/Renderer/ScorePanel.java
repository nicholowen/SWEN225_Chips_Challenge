package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScorePanel extends JPanel {

  /*===============================================================
   * PLACE HOLDER - will show the score board, time, and inventory
   *===============================================================
   * contains some garbage code to draw (can see what I am doing)
   */

  private Image background;
  private BufferedImage[] digits;
  char[] chars = {0, 0, 0};

  public ScorePanel() {
    setPreferredSize(new Dimension(300, 576));
    this.background = loadBackground();
    requestFocus();
    init();
  }

  public void init(){
    digits = Assets.DIGITS[0];
  }

  private BufferedImage loadBackground(){
    return Assets.SCOREBACKGROUND[0][0];
  }

  public void update(int timeLimit){
    String time = String.valueOf(timeLimit);
    chars = time.toCharArray();
//    System.out.println(time);
    repaint();
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(background, 0, 0, this);

    int offset = 0;
    if(chars.length == 2){
      offset = 1;
      g.drawImage(digits[0], 110, 179, this);
    }
    else if(chars.length == 1) {
      offset = 2;
      g.drawImage(digits[0], 110, 179, this);
      g.drawImage(digits[0], 142, 179, this);
    }

    for(int i = 0; i < chars.length; i++){
      int digit = Character.getNumericValue(chars[i]);
      if(digit >= 0) {
        g.drawImage(digits[digit], 110 + ((i + offset) * 32), 179, this);
      }

    }

  }

}
