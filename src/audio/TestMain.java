package audio;

import GUI.InterfaceMain;

public class TestMain {

    public static void main(String[] args){
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
