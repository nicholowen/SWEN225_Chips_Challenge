package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.GamePanel;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.ScorePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Render {

  JFrame frame;
  Canvas canvas;
  GamePanel gp;
  ScorePanel sp;

  public Render(){
    frame = new JFrame();
    frame.setMinimumSize(new Dimension(800, 500));
    frame.setLayout(new GridBagLayout());
    gp = new GamePanel();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    frame.add(gp);
    frame.add(new ScorePanel());
    gbc.gridx = 1;
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.pack();


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//    gp.run();
  }

  public void update(Cell[][] cell, Actor[] actors){
    gp.update(cell, actors);
  }

  public void init(Cell[][] cells){
    gp.initAnimationObjects(cells);
  }

  public JPanel getPanel(){
    return gp;
  }



}
