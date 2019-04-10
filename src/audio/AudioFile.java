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

public class AudioFile implements LineListener{

    private InputStream is;
    private AudioInputStream ais;
    private AudioFormat audioFormat;
    private DataLine.Info DLinfo;
    private Clip soundClip;
    private FloatControl fControl;
    private volatile boolean play;

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
    public void play() {
        play(-9);
    }

    public void play(float volume) {
        fControl.setValue(volume);
        soundClip.start();
        play = true;
    }

    public boolean isPlay() {
        return play;
    }

    public void stopMusic(){
        soundClip.stop();
        soundClip.flush();
        soundClip.setFramePosition(0);
        play = false;
    }
    @Override
    public void update(LineEvent event) {
        if(event.getType() == LineEvent.Type.START)
            play = true;
        else if(event.getType() == LineEvent.Type.STOP) {
            soundClip.stop();
            soundClip.flush();
            soundClip.setFramePosition(0);
            play = false;
        }
    }

}
