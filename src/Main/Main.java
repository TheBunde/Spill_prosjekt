package Main;

import GUI.InterfaceMain;
import audio.MusicPlayer;
import audio.SFXPlayer;
import audio.ThreadPool;
import Database.*;
import GUI.*;

public class Main {

    public static User user;
    public static Database db;
    public static void main(String[] args){
        db = new Database("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=", "q8CeXgyy");
        ThreadPool pool = new ThreadPool(4);

        //MusicPlayer player = new MusicPlayer("testintro");

        SFXPlayer sfx = new SFXPlayer("knockSFX");

        //pool.runTask(player);
        pool.runTask(MusicPlayer.getInstance());
        //sfx.playSound()
        //pool.runTask(sfx);
        pool.runTask(new InterfaceMain());

        pool.join();
    }
}
