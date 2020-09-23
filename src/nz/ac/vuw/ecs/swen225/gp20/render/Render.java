package src.nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.ScorePanel;

import javax.swing.*;
import java.awt.*;

public class Render {

  JFrame frame;
  Canvas canvas;
  GamePanel gp;
  ScorePanel sp;

  public Render(){
    frame = new JFrame();
    frame.setMinimumSize(new Dimension(800, 500));
    frame.setLayout(new FlowLayout());
    gp = new GamePanel();
    frame.add(gp);
    frame.add(new ScorePanel());
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    gp.run();
  }

  public void update(){
    gp.update();
  }



}
