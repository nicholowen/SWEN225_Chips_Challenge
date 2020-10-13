package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {

  boolean play;

  File path = new File("resources/Assets/Audio/");


  public Audio(){ }

  public void update(String soundEvent){
    play(soundEvent);

  }

  public void play(String action){
    String file = null;
    if(action != null) {
      file = action + ".wav";
    }
    if(file != null){
      try {

        AudioInputStream audioIn;
        audioIn = AudioSystem.getAudioInputStream(new File(path, file));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f); // reduce volume (decibels)

        clip.start();

      } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
        e.printStackTrace();
        System.out.println(action);
      }

    }
//    Clip c = AudioSystem.getClip();
//    c.open(clip);
//    c.start();
  }

  public void setPlay(boolean play){
    this.play = play;
  }

  public boolean getPlay(){
    return play;
  }

  public void playMusic() {
    try {
      AudioInputStream audioIn;
      audioIn = AudioSystem.getAudioInputStream(new File(path, "main_theme.wav"));
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);

      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-20.0f); // reduce volume (decibels)

      clip.start();
    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
      e.printStackTrace();
    }
  }



}
