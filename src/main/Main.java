package main;

import GUI.InterfaceMain;
import audio.MusicPlayer;
import audio.ThreadPool;
import database.*;
import user.User;

/**
 * Main-class that runs the application with music
 *
 * @author shahedsa, magnubau, williad, heleneyj, saramoh, henrikwt
 */
public class Main {

    public static User user;
    public static Database db;

    /**
     * Main-method
     * @param args
     */
    public static void main(String[] args){
        db = new Database();

        MusicPlayer.getInstance().changeSong(11);
        ThreadPool.getInstance().runTask(MusicPlayer.getInstance());
        ThreadPool.getInstance().runTask(new InterfaceMain());

        ThreadPool.getInstance().join();
    }
}
