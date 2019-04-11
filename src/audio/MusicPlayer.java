package audio;

import java.util.ArrayList;

/**
 * This class is used to make a playlist of songs
 * that play in the background of the game. The class contains
 * methods to manipulate and access music when required.
 * @author henrikwt
 */

public class MusicPlayer implements Runnable {

    private boolean stop, run;
    private ArrayList<AudioFile> fileList;
    private float volMusic;
    private int currentMusicIndex;

    /* All the music names get added in this MusicPlayer object. */
    private static MusicPlayer thisInstance = new MusicPlayer("testintro","pause", "mainmenu", "battlesongone",
            "warriorSong2","rogueSong","wizardSong","desertwalk","pianosong","pianosong2","testintro3",
            "intro","intro2","rangerSong","snowSong2","lavaSong","forestSong");
    /*
     * List of songs with index:
     * 0: "testintro"
     * 1: "pause"
     * 2: "mainmenu"
     * 3: "battlesongone"
     * 4: "warriorSong"
     * 5: "rogueSong"
     * 6: "wizardSong"
     * 7: "desertwalk"
     * 8: "pianosong"
     * 9: "pianosong2"
     * 10: "testintro3"
     * 11: "intro"
     * 12: "intro2"
     * 13: "rangerSong"
     * 14: "snowSong2"
     * 15: "lavaSong"
     * 16: "forestSong"
     */

    /**
     * Constructor for MusicPlayer.
     * @param musicFiles Music Files.
     */
    private MusicPlayer(String... musicFiles) {
        fileList = new ArrayList<AudioFile>();
        for(String musicFile : musicFiles)
            fileList.add(new AudioFile(musicFile + ".wav"));
    }
    /**
     * @return The single instantiation of MusicPlayer.
     */
    public static MusicPlayer getInstance(){
        return thisInstance;
    }


    /**
     * run() method.
     */
    @Override
    public void run() {
        /* run() starts the audio and also makes sure the audio loops. */
        stop = false;
        run = true;
        AudioFile soundTrack = fileList.get(currentMusicIndex);
        soundTrack.play(volMusic);
        while(run) {
            /* If stopSong() is called then this if statement will run and the audio will stop. */
            if(stop){
                soundTrack.audioStop();
                stop = false;
            }
            /*
             * If the index would get out of bounds and audio would not play
             * this would set it back to play the first song in the playlist
             */
            if(!soundTrack.isPlay()) {
                if(currentMusicIndex >= fileList.size())
                    currentMusicIndex = 0;
                soundTrack = fileList.get(currentMusicIndex);
                soundTrack.play(volMusic);
            }
            /* Waits for 10 milliseconds so that the next song won't try to run before the previous one is done. */
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
    /* changeSong() is used to select which song to play by using the index of the song in the arraylist as the parameter*/

    /**
     * Changes the song that will play next.
     * @param currentMusicIndex Index of song to play.
     */
    public void changeSong(int currentMusicIndex){
        try{
            this.currentMusicIndex = currentMusicIndex;
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    /**
     * Stops the currently playing song.
     */
    public void stopSong(){
        stop = true;
    }

    /*
     * This method is used when one song should play first, but another song should play and then loop after
     * the first song is over, automatically, so the song keeps playing the desired song and not the first one.
     */

    /**
     * Stops the song only if the wanted song is not playing.
     * @param index Index of song to keep playing.
     */
    public void keepPlaying(int index){
        if(currentMusicIndex != index){
            stopSong();
        }
    }

    /**
     * Sets the volume of the music.
     * @param volMusic Volume.
     */
    public void setVolMusic(float volMusic){
        this.volMusic = volMusic;
    }
}