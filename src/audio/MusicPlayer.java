package audio;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.*;


public class MusicPlayer implements Runnable {

    private ArrayList<AudioFile> musicFiles;
    private int currentSongIndex;
    private boolean running;
    private boolean stopSong;
    private float volumeMusic;

    private static MusicPlayer thisInstance = new MusicPlayer("testintro","pause", "mainmenu", "battlesongone","warriorSong","rogueSong","wizardSong");

    public static MusicPlayer getInstance(){
        return thisInstance;
    }

    public MusicPlayer(String... files) {
        musicFiles = new ArrayList<AudioFile>();
        for(String file : files)
            //musicFiles.add(new AudioFile("C:/Users/henri/OneDrive/Documents/Dataingeniør/gitworkshop/Ny Mappe/Game-development-project/src/audio/music/" + file + ".wav"));
            musicFiles.add(new AudioFile("src/audio/music/" + file + ".wav"));
    }

    /*
    public void stopSong(){
        running = false;
    }
    */

    public void stopSong(){
        stopSong = true;
    }

    public void changeSong(int currentSongIndex){
        this.currentSongIndex = currentSongIndex;
    }

    public void setVolumeMusic(float volumeMusic){
        this.volumeMusic = volumeMusic;
    }


    @Override
    public void run() {
        running = true;
        stopSong = false;
        AudioFile song = musicFiles.get(currentSongIndex);
        song.play(volumeMusic);
        while(running) {
            if(stopSong){
                song.stopMusic();
                stopSong = false;

            }

            if(!song.isPlaying()) {
                //currentSongIndex++;
                if(currentSongIndex >= musicFiles.size())
                    currentSongIndex = 0;
                song = musicFiles.get(currentSongIndex);
                song.play(volumeMusic);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    @Override
    public void run() {
        running = true;
        AudioFile song = musicFiles.get(currentSongIndex);
        song.play();
        while(running) {
            if(!song.isPlaying()) {
                currentSongIndex++;
                if(currentSongIndex >= musicFiles.size())
                    currentSongIndex = 0;
                song = musicFiles.get(currentSongIndex);
                song.play();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    */
}