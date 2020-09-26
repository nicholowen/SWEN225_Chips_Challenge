package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import nz.ac.vuw.ecs.swen225.gp20.render.Render;


public class KeyListeners implements KeyListener{

	private boolean recording = false;
	private String direction = null;
	
	public KeyListeners(Render render) {
		render.getFrame().addKeyListener(this);  
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		switch (key) {
		case 'w':
			this.direction = "up";
			System.out.println(direction);
		case 'a':
			this.direction = "left";
			System.out.println(direction);
		case 's':
			this.direction = "down";
			System.out.println(direction);
		case 'd':
			this.direction = "right";
			System.out.println(direction);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.direction = null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		switch (key) {
		case 'r':
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
