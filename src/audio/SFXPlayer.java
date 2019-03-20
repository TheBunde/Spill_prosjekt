package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public class SFXPlayer implements Runnable{

    private ArrayList<AudioFile> soundFiles;

    private int currentSFXIndex;
    private float volumeSFX;
    //TEST START
    private static SFXPlayer thisInstance = new SFXPlayer("knockSFX", "splashSFX","warriorSFX","rogueSFX2","wizardSFX");

    public static SFXPlayer getInstance(){
        return thisInstance;
    }
    ///TEST SLUTT

    /// TO GET MORE SFX TO PLAY AT THE SAME TIME USE: new SFXPlayer("knockSFX").run();

    public SFXPlayer(String... files){
        soundFiles = new ArrayList<AudioFile>();
        for(String file : files)
            //soundFiles.add(new AudioFile("C:/Users/henri/OneDrive/Documents/Dataingeniør/gitworkshop/Ny Mappe/Game-development-project/src/audio/SFX/" + file + ".wav"));
            soundFiles.add(new AudioFile("src/audio/SFX/" + file + ".wav"));
    }

    public void setVolumeSFX(float volumeSFX){
        this.volumeSFX = volumeSFX;
    }

    public void setSFX(int currentSFXIndex){
        AudioFile sfx = soundFiles.get(currentSFXIndex);
        sfx.play(volumeSFX);
        //this.currentSFXIndex = currentSFXIndex;
    }


    @Override
    public void run(){
        AudioFile soundEffect = soundFiles.get(currentSFXIndex);
        soundEffect.play(volumeSFX);
    }


}
