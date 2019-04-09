package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SettingsController {

    @FXML
    private Button backToMenuButton;
    @FXML
    private Slider musicSlider;
    @FXML
    private Slider sfxSlider;
    @FXML
    private SceneSwitcher sceneSwitcher;

    public SettingsController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void backToMenu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");

    }

    public void changeVolumeMusic(){

        double slideValue = musicSlider.getValue();
        float volumeMusic = ((float) slideValue);
        if(volumeMusic < -30.0){
            System.out.println(volumeMusic);
            MusicPlayer.getInstance().setVolMusic(-80);
            MusicPlayer.getInstance().stopSong();
        }
        else{
            System.out.println(volumeMusic);
            MusicPlayer.getInstance().setVolMusic(volumeMusic);
            MusicPlayer.getInstance().stopSong();
        }
        MusicPlayer.getInstance().changeSong(2);


    }

    public void changeVolumeSFX(){

        double slideSFXValue = sfxSlider.getValue();
        float volumeSFX = ((float) slideSFXValue);
        if(volumeSFX < -30.0){
            System.out.println(volumeSFX);
            SFXPlayer.getInstance().setVolSFX(-80);
        }
        else{
            System.out.println(volumeSFX);
            SFXPlayer.getInstance().setVolSFX(volumeSFX);
        }
        SFXPlayer.getInstance().setSFX(0);


    }

}
