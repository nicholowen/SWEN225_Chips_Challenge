package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * Represents the menu states the game can be in.
 * States: - 0: Intro
 *         - 1: Menu
 *         - 2: Levels
 *         - 3: Pause
 *         - 4: Play
 *         - 5: Dead
 *         - 6: Win
 *         - 7: Information
 *
 * @author Owen Nicholson 3001130635
 */
public class State {

  private Assets assets;
  private BufferedImage bg;
  private int state;
  private String stateName;
  //the graphic row
  private BufferedImage[] buttonOne;
  private BufferedImage[] buttonTwo;
  private BufferedImage[] buttonThree;
  private BufferedImage[] buttonFour;
  private BufferedImage[] buttonFive;
  //current state
  private BufferedImage currentStateOne;
  private BufferedImage currentStateTwo;
  private BufferedImage currentStateThree;
  private BufferedImage currentStateFour;
  private BufferedImage currentStateFive;

  public State(Assets assets, int state, String stateName) {
    this.state = state;
    this.stateName = stateName;
    this.assets = assets;
    init();
  }

  /**
   * Initialises all assets. Depending on the state, it will add certain buttons.
   */
  private void init() {
    String background = stateName + "Background";
    String buttonSet = stateName + "Buttons";
    this.bg = assets.getAsset(background)[0][0];
    BufferedImage[][] buttonGraphics = assets.getAsset(buttonSet);

    if(state == 1){ // 4 buttons active
      this.buttonOne = buttonGraphics[0];
      this.buttonTwo = buttonGraphics[1];
      this.buttonThree = buttonGraphics[2];
      this.buttonFive = buttonGraphics[3];
      this.currentStateOne = buttonOne[0];
      this.currentStateTwo = buttonTwo[0];
      this.currentStateThree = buttonThree[0];
      this.currentStateFive = buttonFive[0];
    }else if(state == 3){ // 5 buttons active
      this.buttonOne = buttonGraphics[0];
      this.buttonTwo = buttonGraphics[1];
      this.buttonThree = buttonGraphics[2];
      this.buttonFour = buttonGraphics[3];
      this.buttonFive = buttonGraphics[4];
      this.currentStateOne = buttonOne[0];
      this.currentStateTwo = buttonTwo[0];
      this.currentStateThree = buttonThree[0];
      this.currentStateFour = buttonFour[0];
      this.currentStateFive = buttonFive[0];
    }else if (state == 2 || state == 5) { // 3 buttons active
      this.buttonOne = buttonGraphics[0];
      this.buttonTwo = buttonGraphics[1];
      this.buttonThree = buttonGraphics[2];
      this.currentStateOne = buttonOne[0];
      this.currentStateTwo = buttonTwo[0];
      this.currentStateThree = buttonThree[0];
    }else if (state == 6) { // 2 buttons active
      this.buttonOne = buttonGraphics[0];
      this.buttonTwo = buttonGraphics[1];
      this.currentStateOne = buttonOne[0];
      this.currentStateTwo = buttonTwo[0];
    }else if (state == 7) { // 1 button active (but specific position)
      this.buttonFour = buttonGraphics[0];
      this.currentStateFour = buttonFour[0];
    }
  }

  /**
   * Returns the button graphics back to the button state.
   * Has to check if it's null since different states have different number of buttons.
   */
  private void resetButtonStates() {
    if (currentStateOne != null) this.currentStateOne = buttonOne[0];
    if (currentStateTwo != null) this.currentStateTwo = buttonTwo[0];
    if (currentStateThree != null) this.currentStateThree = buttonThree[0];
    if (currentStateFour != null) this.currentStateFour = buttonFour[0];
    if (currentStateFive != null) this.currentStateFive = buttonFive[0];
  }

  /**
   * Checks to see if any of the buttons are being hovered over pressed and
   * updates the graphics accordingly.
   *
   * @param buttonEvent String that contains the button number and the state it is in.
   */
  public void update(String buttonEvent) {

    if (buttonEvent != null) {
      if (currentStateOne != null && buttonEvent.contains("one")) {
        currentStateOne =  checkState(buttonEvent, buttonOne);
      } else if (currentStateTwo != null && buttonEvent.contains("two")) {
        currentStateTwo = checkState(buttonEvent, buttonTwo);
      } else if (currentStateThree != null && buttonEvent.contains("three")) {
         currentStateThree = checkState(buttonEvent, buttonThree);
      } else if (currentStateFour != null && buttonEvent.contains("four")) {
        currentStateFour = checkState(buttonEvent, buttonFour);
      } else if (currentStateFive != null && buttonEvent.contains("pause")) {
        currentStateFive = checkState(buttonEvent, buttonFive);
      }
    } else {
      resetButtonStates();
    }
  }

  /**
   * Checks to see what the button state is.
   * If the mouse is over the button, it will receive a 'hover' tag.
   * If the mouse is pressed, it will receive a 'pressed' tag.
   *
   * @param buttonEvent The tag that determines what action state the button is in
   * @param sheet The image sheet
   * @return Buffered image of the state the button is in. (highlighted or being pressed).
   */
  public BufferedImage checkState(String buttonEvent, BufferedImage[] sheet){
    if (buttonEvent.contains("hover")) return sheet[1];
    else if (buttonEvent.contains("pressed")) return sheet[2];
    else return null;
  }

  /**
   * Draws the background and buttons in known positions.
   *
   * @param g Graphics object.
   */
  public void draw(Graphics g) {
    if (bg != null) g.drawImage(bg, 0, 0, null);
    if (currentStateOne   != null) g.drawImage(currentStateOne,   365, 268, null);
    if (currentStateTwo   != null) g.drawImage(currentStateTwo,   365, 336, null);
    if (currentStateThree != null) g.drawImage(currentStateThree, 365, 404, null);
    if (currentStateFour  != null) g.drawImage(currentStateFour,  365, 472, null);
    if (currentStateFive  != null) g.drawImage(currentStateFive,  660, 527, null);
  }
}
