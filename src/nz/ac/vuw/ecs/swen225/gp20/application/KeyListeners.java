package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import nz.ac.vuw.ecs.swen225.gp20.render.Render;


public class KeyListeners implements KeyListener{

	private boolean recording = false;
	private String direction = null;
	
	public KeyListeners(Render render) {
        render.getGp().addKeyListener(this); 
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Unused 
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.direction = null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	    char key = e.getKeyChar();
        switch (key) {
        if(key == 'w') {
            this.direction = "up";
            System.out.println(direction);
        }
        else if(key == 'a') {
            this.direction = "left";
            System.out.println(direction);
        }
        else if(key == 's') {
            this.direction = "down";
            System.out.println(direction);
        }
        else if(key == 'd') {
            this.direction = "right";
            System.out.println(direction);
        }
        else if(key == 'r') {
			if(recording) recording = true;
			else recording = false;
		}
	}
	
    /*===================================================
    *	Getters and Setters
    * ===================================================*/
    public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
