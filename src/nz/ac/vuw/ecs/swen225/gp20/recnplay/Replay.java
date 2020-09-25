package nz.ac.vuw.ecs.swen225.gp20.recnplay;

// TODO all imports needed:
//         1. maze's board and player (???)
//         2. renderer

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;

import javax.naming.event.NamingExceptionEvent;
import javax.swing.*;
import java.util.Observer;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Observer;
import java.util.Queue;
import java.util.Scanner;

public class Replay { // extends Obervable implements Observer
    File JSONsaveFile;
    private Queue<Record> moves = new ArrayDeque<>();

    public Replay() {
        if(this.replay()) this.read();
    }

    /**
     * This method handles the actual game replays.
     */
    public boolean replay() {
        assert JSONsaveFile == null;
        this.JSONsaveFile = new File("persistence_game_recording.json");
        return true;
    }

    public void read() {
        FileInputStream fileInputStream;
        String string;

        assert JSONsaveFile != null;
        try {
            fileInputStream = new FileInputStream(JSONsaveFile);
            byte [] saveData = new byte[(int) JSONsaveFile.length()];
            fileInputStream.read(saveData);
            fileInputStream.close();
            string = new String(saveData, StandardCharsets.UTF_8);
        } catch(Exception e) { return; }

        assert string != null;
        Scanner s = new Scanner(string);


    }

//    @Override
    public void update(java.util.Observable observable, Object o) {
//        if (o instanceof ) {
//        }
    }
}
