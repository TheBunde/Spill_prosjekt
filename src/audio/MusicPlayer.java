package audio;

import java.util.ArrayList;


public class MusicPlayer implements Runnable {

    private ArrayList<AudioFile> fileList;
    private int currentSongIndex;
    private boolean run;
    private boolean stop;
    private float volMusic;

    private static MusicPlayer thisInstance = new MusicPlayer("testintro","pause", "mainmenu", "battlesongone",
            "warriorSong2","rogueSong","wizardSong","desertwalk","pianosong","pianosong2","testintro3",
            "intro","intro2","rangerSong","snowSong2","lavaSong","forestSong");
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
    16: "forestSong"
     */

    public MusicPlayer(String... files) {
        fileList = new ArrayList<AudioFile>();
        for(String file : files)
            fileList.add(new AudioFile("src/audio/music/" + file + ".wav"));
    }

    public void stopSong(){
        stop = true;
    }

    public void changeSong(int currentSongIndex){
        try{
            this.currentSongIndex = currentSongIndex;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setVolMusic(float volMusic){
        this.volMusic = volMusic;
    }

    @Override
    public void run() {
        run = true;
        stop = false;
        AudioFile song = fileList.get(currentSongIndex);
        song.play(volMusic);
        while(run) {
            if(stop){
                song.stopMusic();
                stop = false;
            }
            if(!song.isPlaying()) {
                if(currentSongIndex >= fileList.size())
                    currentSongIndex = 0;
                song = fileList.get(currentSongIndex);
                song.play(volMusic);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void keepPlaying(int index){
        if(currentSongIndex != index){
            stopSong();
        }
    }
}