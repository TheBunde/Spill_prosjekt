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

    private static MusicPlayer thisInstance = new MusicPlayer("testintro","pause", "mainmenu", "battlesongone",
            "warriorSong2","rogueSong","wizardSong","desertwalk","pianosong","pianosong2","testintro3",
            "intro","intro2","rangerSong","snowSong2","lavaSong");
    public static MusicPlayer getInstance(){
        return thisInstance;
    }

    /*
    List of songs with index:
    0: "testintro"
    1: "pause"
    2: "mainmenu"
    3: "battlesongone"
    4: "warriorSong"
    5: "rogueSong"
    6: "wizardSong"
    7: "desertwalk"
    8: "pianosong"
    9: "pianosong2"
    10: "testintro3"
    11: "intro"
    12: "intro2"
    13: "rangerSong"
    14: "snowSong(2)"
    15: "lavaSong"

     */

    public MusicPlayer(String... files) {
        musicFiles = new ArrayList<AudioFile>();
        for(String file : files)
            musicFiles.add(new AudioFile("src/audio/music/" + file + ".wav"));
    }

    public void stopSong(){
        stopSong = true;
    }

    public void changeSong(int currentSongIndex){
        try{
            this.currentSongIndex = currentSongIndex;
        }catch (Exception e){
            e.printStackTrace();
        }

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
}