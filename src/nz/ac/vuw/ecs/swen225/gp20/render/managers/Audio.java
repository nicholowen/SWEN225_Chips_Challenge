package nz.ac.vuw.ecs.swen225.gp20.render.managers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Audio Player - plays the music when constructed (i.e. the game starts), then loops the main theme.
 * Will play the sound for the corresponding sound event depending on the string provided.
 */
public class Audio {

  File path = new File("resources/Assets/Audio/");
  long time;
  long themeLength;


  public Audio(){
    playMusic();
  }

  /**
   * Plays the sound effect.
   * @param soundEvent String representing the sound
   */
  public void update(String soundEvent){
    play(soundEvent);
  }

  /**
   * Plays the sound for the particular sound event.
   * @param action String representing the sound
   */
  public void play(String action){

    String file = null;

    //Checks to see if there is a hover or pressed tag - this is for button actions.
    if (action != null) {
      if (action.contains("hover")){
        action = "hover";
      } else if (action.contains("pressed")){
        action = "pressed";
      }
      file = action + ".wav";
    }
    if (file != null){
      try {

        AudioInputStream audioIn;
        System.out.println(file);
        audioIn = AudioSystem.getAudioInputStream(new File(path, file));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f); // reduce volume (decibels)

        clip.start();

      } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
        e.printStackTrace();
        System.out.println("Audio not found for: " + action);
      }

    }
  }

  /**
   * Dedicated method for playing the theme music. It loops continuously (until the game is exited).
   */
  public void playMusic() {
    time = System.currentTimeMillis();
    try {
      AudioInputStream audioIn;
      audioIn = AudioSystem.getAudioInputStream(new File(path, "main_theme.wav"));
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      themeLength = clip.getMicrosecondLength()/1000; //convert time from microseconds to milliseconds
      clip.loop(Clip.LOOP_CONTINUOUSLY);

      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-25.0f); // reduce volume (decibels)

      clip.start();
    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
      e.printStackTrace();
    }
  }



}
