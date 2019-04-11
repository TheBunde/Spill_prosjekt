package audio;

import java.util.ArrayList;

/**
 * This class used to make a playlist of sound effects (SFX)
 * to be used in the game. The class contains
 * methods to manipulate and access the SFX when required.
 * @author henrikwt
 */

public class SFXPlayer implements Runnable{

    private int currentSFXIndex;
    private float volSFX;
    private ArrayList<AudioFile> fileList;

    /* All the SFX names get added in this SFXPlayer object. */
    private static SFXPlayer thisInstance = new SFXPlayer("knockSFX", "splashSFX","warriorSFX","rogueSFX2","wizardSFX",
        "noKeySFX","wrongKeySFX","correctKeySFX","rangerSFX","arrowSFX","enmhitSFX","enmmissSFX","pdefSFX","victory1SFX"
    ,"defeat", "moveSFX","gameWonSFX");

    public static SFXPlayer getInstance(){
        return thisInstance;
    }

    /*
     * List of SFX and index:
     * 0: "knockSFX"
     * 1: "splashSFX"
     * 2: "warriorSFX"
     * 3: "rogueSFX2"
     * 4: "wizardSFX"
     * 5: "noKeySFX"
     * 6: "wrongKeySFX"
     * 7: "correctKeySFX"
     * 8: "rangerSFX"
     * 9: "arrowSFX"
     * 10: "enmhitSFX"
     * 11: "enmmissSFX"
     * 12: "pdefSFX"
     * 13: "victory1SFX"
     * 14: "defeat"
     * 15: "moveSFX"
     * 16: "gameWonSFX"
     */

    /**
     * Constructor for SFXPlayer.
     * @param sfxFiles SFX Files.
     */
    public SFXPlayer(String... sfxFiles){
        fileList = new ArrayList<AudioFile>();
        for(String sfxFile : sfxFiles)
            fileList.add(new AudioFile(sfxFile + ".wav"));
    }

    /**
     * run() method.
     */
    @Override
    public void run(){
        AudioFile soundEffect = fileList.get(currentSFXIndex);
        soundEffect.play(volSFX);
    }

    /**
     * Sets the volume of the SFX.
     * @param volSFX Volume.
     */
    public void setVolSFX(float volSFX){
        this.volSFX = volSFX;
    }

    /**
     * Plays the SFX.
     * @param currentSFXIndex Index of SFX.
     */
    public void setSFX(int currentSFXIndex){
        AudioFile sfx = fileList.get(currentSFXIndex);
        sfx.play(volSFX);
    }
}

