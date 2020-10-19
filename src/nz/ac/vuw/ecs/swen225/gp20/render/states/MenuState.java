package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents the menu state (title screen) Will lead to other menu items or exit.
 *
 * @author Owen Nicholson 300130653
 */
public class MenuState {

  Assets assets;
  BufferedImage bg;

  BufferedImage[][] buttonGraphics; //all button graphics
  //the graphic row
  BufferedImage[] buttonOne;
  BufferedImage[] buttonTwo;
  BufferedImage[] buttonThree;
  BufferedImage[] buttonFour;
  //current state
  BufferedImage currentStateOne;
  BufferedImage currentStateTwo;
  BufferedImage currentStateThree;
  BufferedImage currentStateFour;

  public MenuState(Assets assets){
    this.assets = assets;
    init();
  }

  /**
   * initialises all graphics for this state.
   */
  private void init(){
    this.bg = assets.getAsset("menuBackground")[0][0];
    this.buttonGraphics = assets.getAsset("menuButtons");

    this.buttonOne = buttonGraphics[0];
    this.buttonTwo = buttonGraphics[1];
    this.buttonThree = buttonGraphics[2];
    this.buttonFour = buttonGraphics[3];
    this.currentStateOne = buttonOne[0];
    this.currentStateTwo = buttonTwo[0];
    this.currentStateThree = buttonThree[0];
    this.currentStateFour = buttonFour[0];
  }

  /**
   * Resets all buttons to their resting state (default).
   */
  private void resetButtonStates(){
    this.currentStateOne = buttonOne[0];
    this.currentStateTwo = buttonTwo[0];
    this.currentStateThree = buttonThree[0];
    this.currentStateFour = buttonFour[0];
  }

  /**
   * Updates the state of the button depending on the button state
   * @param buttonEvent A string containing both the button number 1->4 and the state it is in.
   */
  public void update(String buttonEvent){
    if(buttonEvent != null){
      if(buttonEvent.contains("one")){
        if (buttonEvent.contains("hover")) currentStateOne = buttonOne[1];
        else if (buttonEvent.contains("pressed")) currentStateOne = buttonOne[2];
      }else if (buttonEvent.contains("two")){
        if (buttonEvent.contains("hover")) currentStateTwo = buttonTwo[1];
        else if (buttonEvent.contains("pressed")) currentStateTwo = buttonTwo[2];
      }else if (buttonEvent.contains("three")){
        if (buttonEvent.contains("hover")) currentStateThree = buttonThree[1];
        else if (buttonEvent.contains("pressed")) currentStateThree = buttonThree[2];
      }else if (buttonEvent.contains("four")){
        if (buttonEvent.contains("hover")) currentStateFour = buttonFour[1];
        else if (buttonEvent.contains("pressed")) currentStateFour = buttonFour[2];
      }
    }else{
      resetButtonStates();
    }
  }

  public void draw(Graphics g) {
    g.drawImage(bg, 0, 0, null);
    g.drawImage(currentStateOne, 365, 268, null);
    g.drawImage(currentStateTwo, 365, 336, null);
    g.drawImage(currentStateThree, 365, 404, null);
    g.drawImage(currentStateFour, 365, 472, null);
  }

}
