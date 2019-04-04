package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public class SFXPlayer implements Runnable{

    private ArrayList<AudioFile> soundFiles;

    private int currentSFXIndex;
    private float volumeSFX;

    private static SFXPlayer thisInstance = new SFXPlayer("knockSFX", "splashSFX","warriorSFX","rogueSFX2","wizardSFX",
        "noKeySFX","wrongKeySFX","correctKeySFX","rangerSFX","arrowSFX","enmhitSFX","enmmissSFX","pdefSFX","victory1SFX" );

    public static SFXPlayer getInstance(){
        return thisInstance;
    }

    /*
    List of SFX and index:
    0: "knockSFX"
    1: "splashSFX"
    2: "warriorSFX"
    3: "rogueSFX2"
    4: "wizardSFX"
    5: "noKeySFX"
    6: "wrongKeySFX"
    7: "correctKeySFX"
    8: "rangerSFX"
    9: "arrowSFX"
    10: "enmhitSFX"
    11: "enmmissSFX"
    12: "pdefSFX"
    13: "victory1SFX"
     */

    public SFXPlayer(String... files){
        soundFiles = new ArrayList<AudioFile>();
        for(String file : files)
            soundFiles.add(new AudioFile("src/audio/SFX/" + file + ".wav"));
    }

    public void setVolumeSFX(float volumeSFX){
        this.volumeSFX = volumeSFX;
    }

    public void setSFX(int currentSFXIndex){
        AudioFile sfx = soundFiles.get(currentSFXIndex);
        sfx.play(volumeSFX);
    }
    @Override
    public void run(){
        AudioFile soundEffect = soundFiles.get(currentSFXIndex);
        soundEffect.play(volumeSFX);
    }
}
