package audio;

import java.util.ArrayList;

public class SFXPlayer implements Runnable{

    private ArrayList<AudioFile> fileList;

    private int currentSFXIndex;
    private float volSFX;

    private static SFXPlayer thisInstance = new SFXPlayer("knockSFX", "splashSFX","warriorSFX","rogueSFX2","wizardSFX",
        "noKeySFX","wrongKeySFX","correctKeySFX","rangerSFX","arrowSFX","enmhitSFX","enmmissSFX","pdefSFX","victory1SFX"
    ,"defeat", "moveSFX","gameWonSFX");

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
    14: "defeat"
    15: "moveSFX"
    16: "gameWonSFX"
     */

    public SFXPlayer(String... files){
        fileList = new ArrayList<AudioFile>();
        for(String file : files)
            fileList.add(new AudioFile("src/audio/SFX/" + file + ".wav"));
    }

    public void setVolSFX(float volSFX){
        this.volSFX = volSFX;
    }

    public void setSFX(int currentSFXIndex){
        AudioFile sfx = fileList.get(currentSFXIndex);
        sfx.play(volSFX);
    }
    @Override
    public void run(){
        AudioFile soundEffect = fileList.get(currentSFXIndex);
        soundEffect.play(volSFX);
    }
}
