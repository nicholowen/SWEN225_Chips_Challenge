package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * Handles the pause screen.
 * @author Owen Nicholson 300130653
 */
public class PauseState {

  Assets assets;

  BufferedImage bg;
  BufferedImage[] buttonOne;
  BufferedImage[] buttonTwo;
  BufferedImage currentStateOne;
  BufferedImage currentStateTwo;

  public PauseState(Assets assets){
    this.assets = assets;
    init();
  }

  private void init(){
    this.bg = assets.getAsset("pauseBackground")[0][0];
    this.buttonOne = assets.getAsset("pauseButtons")[0];
    this.buttonTwo = assets.getAsset("pauseButtons")[1];
    this.currentStateOne = buttonOne[0];
    this.currentStateTwo = buttonTwo[0];
  }

  private void resetButtonStates(){
    this.currentStateOne = buttonOne[0];
    this.currentStateTwo = buttonTwo[0];
  }

  /**
   * Updates, thus repaints the graphics object.
   * TODO: include button detection so the button can be animated (highlighted and/or depressed)
   */

  public void update(String buttonEvent){
    if(buttonEvent != null){
      if(buttonEvent.contains("one")) {
        if (buttonEvent.contains("hover")) currentStateOne = buttonOne[1];
        else if (buttonEvent.contains("pressed")) currentStateOne = buttonOne[2];
      }else if (buttonEvent.contains("two")){
        if (buttonEvent.contains("hover")) currentStateTwo = buttonTwo[1];
        else if (buttonEvent.contains("pressed")) currentStateTwo = buttonTwo[2];
      }
    }else{
      resetButtonStates();
    }
  }

  public void draw(Graphics g) {
    g.drawImage(bg, 0, 0, null);
    g.drawImage(currentStateOne, 365, 268, null);
    g.drawImage(currentStateTwo, 365, 336, null);
  }

}
