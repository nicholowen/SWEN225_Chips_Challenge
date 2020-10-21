package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * Represents the menu states the game can be in.
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
  //current state
  private BufferedImage currentStateOne;
  private BufferedImage currentStateTwo;
  private BufferedImage currentStateThree;
  private BufferedImage currentStateFour;

  public State(Assets assets, int state, String stateName) {
    this.state = state;
    this.stateName = stateName;
    this.assets = assets;
    init();
  }

  /**
   * Initialises all assets.
   */
  private void init() {
    String background = stateName + "Background";
    String buttonSet = stateName + "Buttons";
    this.bg = assets.getAsset(background)[0][0];
    BufferedImage[][] buttonGraphics = assets.getAsset(buttonSet);

    switch (state) {
      case 1: //state 1 (menu) has 4 buttons
        this.buttonOne = buttonGraphics[0];
        this.buttonTwo = buttonGraphics[1];
        this.buttonThree = buttonGraphics[2];
        this.buttonFour = buttonGraphics[3];
        this.currentStateOne = buttonOne[0];
        this.currentStateTwo = buttonTwo[0];
        this.currentStateThree = buttonThree[0];
        this.currentStateFour = buttonFour[0];
        break;
      case 2: //state 2 (levelSelect) has 3 buttons
        this.buttonOne = buttonGraphics[0];
        this.buttonTwo = buttonGraphics[1];
        this.buttonThree = buttonGraphics[2];
        this.currentStateOne = buttonOne[0];
        this.currentStateTwo = buttonTwo[0];
        this.currentStateThree = buttonThree[0];
        break;
      case 3: //state 1 (pause) has 2 buttons
        this.buttonOne = buttonGraphics[0];
        this.buttonTwo = buttonGraphics[1];
        this.currentStateOne = buttonOne[0];
        this.currentStateTwo = buttonTwo[0];
      default:
        break;
    }
  }

  /**
   * Returns the button graphics back to the default state.
   * Has to check if it's null since different states have different number of buttons.
   */
  private void resetButtonStates() {
    if (currentStateOne != null) this.currentStateOne = buttonOne[0];
    if (currentStateTwo != null) this.currentStateTwo = buttonTwo[0];
    if (currentStateThree != null) this.currentStateThree = buttonThree[0];
    if (currentStateFour != null) this.currentStateFour = buttonFour[0];
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
      }
    } else {
      resetButtonStates();
    }
  }

  public BufferedImage checkState(String buttonEvent, BufferedImage[] sheet){
    if (buttonEvent.contains("hover")) return sheet[1];
    else if (buttonEvent.contains("pressed")) return sheet[2];
    else return null;
  }

  /**
   * Draws the background and buttons.
   *
   * @param g Graphics object.
   */
  public void draw(Graphics g) {
    if (bg != null) g.drawImage(bg, 0, 0, null);
    if (currentStateOne != null) g.drawImage(currentStateOne, 365, 268, null);
    if (currentStateTwo != null) g.drawImage(currentStateTwo, 365, 336, null);
    if (currentStateThree != null) g.drawImage(currentStateThree, 365, 404, null);
    if (currentStateFour != null) g.drawImage(currentStateFour, 365, 472, null);
  }
}