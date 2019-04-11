package audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * This class is used to make audio files playable.
 * @author henrikwt
 */

public class AudioFile implements LineListener{


    private AudioFormat audioFormat;
    private AudioInputStream ais;
    private volatile boolean play;
    private Clip soundClip;
    private DataLine.Info DLinfo;
    private FloatControl fControl;
    private InputStream is;

    /**
     * Constructor for AudioFile.
     * @param fileName Name of the file.
     */
    public AudioFile(String fileName) {
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            audioFormat = ais.getFormat();
            DLinfo = new DataLine.Info(Clip.class, audioFormat);
            soundClip = (Clip) AudioSystem.getLine(DLinfo);
            soundClip.addLineListener(this);
            soundClip.open(ais);
            fControl = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
            ais.close();
            is.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Plays the audio, with set volume parameter.
     * @param db Value in decibel to add or subtract from original volume.
     */
    public void play(float db) {
        fControl.setValue(db);
        soundClip.start();
        play = true;
    }

    /**
     * Plays the audio, with a standard volume.
     */
    public void play() {
        play(-9);
    }

    /**
     * Stops the audio.
     */
    public void audioStop(){
        soundClip.stop();
        soundClip.flush();
        soundClip.setFramePosition(0);
        play = false;
    }

    /**
     * Checks if the audio is playing or not.
     * @return Whether the audio is playing or not.
     */
    public boolean isPlay() {
        return play;
    }

    /**
     * Line Event update.
     * @param lEvent Line Event.
     */
    @Override
    public void update(LineEvent lEvent) {
        if(lEvent.getType() == LineEvent.Type.START)
            play = true;
        else if(lEvent.getType() == LineEvent.Type.STOP) {
            soundClip.stop();
            soundClip.flush();
            soundClip.setFramePosition(0);
            play = false;
        }
    }
}
