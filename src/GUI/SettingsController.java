package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
/**
 * Controller class for the settings menu. Allows users to adjust the volume of the audio.
 * @author henrikwt
 */
public class SettingsController {

    @FXML
    private Button backToMenuButton;
    @FXML
    private Slider musicSlider;
    @FXML
    private Slider sfxSlider;
    @FXML
    private SceneSwitcher sceneSwitcher;
    /**
     * Constructor.
     */
    public SettingsController(){
        sceneSwitcher = new SceneSwitcher();
    }
    /**
     * Takes the user back to the main menu when the designated button is pressed.
     * @throws Exception
     */
    public void backToMenu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");

    }
    /**
     * Adjusts the volume of the music with a slider button.
     */
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
    /**
     * Adjusts the volume of the sound effects with a slider button.
     */
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
