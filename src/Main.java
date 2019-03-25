import GUI.InterfaceMain;
import audio.MusicPlayer;
import audio.SFXPlayer;
import audio.ThreadPool;

public class Main implements Runnable {

    public void run(){
        System.out.println("HEI");
        System.out.println("HEI");
        System.out.println("HEI");
    }

    private static Runnable hei() {
        System.out.println("TEST");
        return null;
    }

    private static Runnable to() {
        System.out.println("TOERN FUNKER");
        return null;
    }



    public static void main(String[] args){
        ThreadPool pool = new ThreadPool(5);

        SFXPlayer sfx = new SFXPlayer("knockSFX");
        //audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(10);

        pool.runTask(MusicPlayer.getInstance());

        pool.runTask(to());

        pool.runTask(new InterfaceMain());

        pool.runTask(new Main());

        pool.join();

        pool.runTask(hei());

        pool.join();
    }
}
