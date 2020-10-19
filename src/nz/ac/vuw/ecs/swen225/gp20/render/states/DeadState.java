package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents the dead state - you died.
 */
public class DeadState {

  Assets assets;

  BufferedImage bg;
  BufferedImage[] buttonOne;
  BufferedImage[] buttonTwo;
  BufferedImage currentStateOne;
  BufferedImage currentStateTwo;

  public DeadState(Assets assets){
    this.assets = assets;
    init();
  }

  /**
   * Initialises all assets.
   */
  private void init(){
    this.bg = assets.getAsset("deadBackground")[0][0];
    this.buttonOne = assets.getAsset("deadButtons")[0];
    this.buttonTwo = assets.getAsset("deadButtons")[1];
    this.currentStateOne = buttonOne[0];
    this.currentStateTwo = buttonTwo[0];
  }

  /**
   * Returns the button graphics back to the default state.
   */
  private void resetButtonStates(){
    this.currentStateOne = buttonOne[0];
    this.currentStateTwo = buttonTwo[0];
  }

  /**
   * Checks to see if any of the buttons are being hovered over pressed and
   * updates the graphics accordingly.
   * @param buttonEvent String that contains the button number and the state it is in.
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
