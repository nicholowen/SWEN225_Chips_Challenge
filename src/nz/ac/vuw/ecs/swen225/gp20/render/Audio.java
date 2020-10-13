package nz.ac.vuw.ecs.swen225.gp20.render;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {

  boolean play;

  public Audio(){


  }

  public void play(String action){
    String file = null;
    if(action.equals("step")){
      file = "sfx_step3.wav";
    }
    if(file != null){
      try {

        File path = new File("resources/Assets/Images/");
        AudioInputStream audioIn;
        audioIn = AudioSystem.getAudioInputStream(new File(path, file));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f); // reduce volume (decibels)

        clip.start();

      } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
        e.printStackTrace();
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

      File path = new File("resources/Assets/Images/");
      AudioInputStream audioIn;
      audioIn = AudioSystem.getAudioInputStream(new File(path, "Chiptune_Journal_Flashback1992 (1).wav"));
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
