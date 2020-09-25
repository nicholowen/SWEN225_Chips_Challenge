package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.event.KeyEvent;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.*;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Record;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

public class Main {
    private static Maze maze;//These are only static for now, cna change if it proves to be an issue.
    private static Render render;//That said, these are never going to be deleted and replaced, so static may be better.
    private static Record rec;
    private static Replay repl;
    private static Persistence persist;
    private static boolean gameEnded;
	private boolean recording = false;
	private String direction = null;


    public static void main(String[] args){
        persist = new Persistence();//Persistance may need to be a parameter for Maze/Record so set it up first! Change if necessary.
        maze = new Maze(persist);
        render = new Render();
        rec = new Record(this);
        repl = new Replay();
		
		render.getFrame().addKeyListener();
		play();
    }

    /**
     * Tick based loop. The main game runs on this loop.
     */
    public static void play() {
        maze.loadMaze(1);
        render.init(maze.getBoard());
    	while(true){
    		if(gameEnded) break;
            render.update(maze.getBoard(), maze.getActors());
    		long start = System.currentTimeMillis(); 
    		while(true) {
    			int delay = 33;
    			if(System.currentTimeMillis() >= start + delay) break; // wait 33 milli
    		}
    	}
    }

    
    /*===================================================
    *	Action Listeners
    * ===================================================*/
    public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		switch (key) {
		case 'w':
			this.direction = "up";
		case 'a':
			this.direction = "left";
		case 's':
			this.direction = "down";
		case 'd':
			this.direction = "right";
		}
	}

	public void keyReleased(KeyEvent e) {
		this.direction = null;
	}

	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		switch (key) {
		case 'r':
			if(recording) recording = true;
			else recording = false;
		}
	}
}
