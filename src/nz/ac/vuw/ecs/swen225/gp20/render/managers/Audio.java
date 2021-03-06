package nz.ac.vuw.ecs.swen225.gp20.render.managers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Audio Player - plays the music when constructed (i.e. the game starts), then loops the main theme.
 * Will play the sound for the corresponding sound event depending on the string provided.
 *
 * Audio clip credit: SubspaceAudio https://opengameart.org/content/512-sound-effects-8-bit-style
 * Main music courtesy of Lex Ashurst-Smoult.
 *
 * @author Owen Nicholson 300130635
 */
public class Audio {

  File path = new File("resources/Assets/Audio/");
  String prevButtonEvent;


  public Audio() {
    playMusic();
  }

  /**
   * Plays the sound effect.
   *
   * @param soundEvent String representing the sound.
   */
  public void updateButtons(String soundEvent) {
    buttonSounds(soundEvent);
  }

  public void updateGame(String soundEvent) {
    gameSounds(soundEvent);
  }

  /**
   * Plays the audio for the particular button event.
   *
   * @param action String representing the sound
   */
  public void buttonSounds(String action) {

    String file;

    //Checks to see if there is a hover or pressed tag - this is for button actions.
    if (action != null) {
      if (action.contains("hover")) {
        if (prevButtonEvent == null) {
          action = "hover";
          prevButtonEvent = "hover";
        } else if (prevButtonEvent.equals("hover")) {
          return;
        }
      } else if (action.contains("pressed")) {
        if (prevButtonEvent != null && prevButtonEvent.equals("pressed")) {
          return;
        }
        action = "pressed";
        prevButtonEvent = "pressed";
      }
      file = action + ".wav";
    } else {
      prevButtonEvent = null;
      return;
    }
    play(file);
  }

  /**
   * Plays the audio for game events - moving, collecting items etc.
   * @param action String representing the sound.
   */
  public void gameSounds(String action) {
    String file = null;
    if (action != null) {
      file = action + ".wav";
    }
    if (file != null) {
      play(file);
    }
  }

  /**
   * Plays the sound effect based on event.
   * @param file The file name of the audio clip.
   */
  private void play(String file){

    try {
      AudioInputStream audioIn;
      audioIn = AudioSystem.getAudioInputStream(new File(path, file));
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-20.0f); // reduce volume (decibels)

      clip.start();

    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
      e.printStackTrace();
      System.out.println("Audio not found for: " + file);
    }

  }

  /**
   * Dedicated method for playing the theme music. It loops continuously (until the game is exited).
   */
  public void playMusic() {
    try {
      AudioInputStream audioIn;
      audioIn = AudioSystem.getAudioInputStream(new File(path, "main_theme.wav"));
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      clip.loop(Clip.LOOP_CONTINUOUSLY);

      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-25.0f); // reduce volume (decibels)

      clip.start();
    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
      e.printStackTrace();
    }
  }
}
