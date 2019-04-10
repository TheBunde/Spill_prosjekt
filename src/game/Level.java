package game;

import main.*;
import audio.MusicPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Contains information about the current level the game is at
 *
 * @author williad, henrikwt
 */
public class Level {
    private int levelId;
    private int music;
    private String backgroundUrl;
    public ImageView backgroundImage;

    /**
     * First constructor for Level
     * Used to manually set the object-variables
     *
     * @param levelId       id for the level
     * @param music         index for the music to be played
     * @param backgroundUrl url for the level-image
     */
    public Level(int levelId, int music, String backgroundUrl){
        this.levelId = levelId;
        this.music = music;
        this.backgroundUrl = backgroundUrl;
        if (backgroundUrl != null) {
            this.backgroundImage = new ImageView(new Image("GUI/images/" + backgroundUrl));
        }
    }

    /**
     * Second constructor
     * Used to set the object variables from the database
     *
     * @param levelId   id for the level
     */
    public Level(int levelId){
        Level firstLevel = Main.db.fetchLevelObject(levelId);
        this.setLevelId(firstLevel.getLevelId());
        this.setMusic(firstLevel.getMusic());
        this.setBackgroundUrl(firstLevel.getBackgroundUrl());
        this.backgroundImage = new ImageView(new Image("GUI/images/" + firstLevel.getBackgroundUrl()));
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(this.music);
    }

    /**
     * @return the level id
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * @param levelId the level id to set
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the music index
     */
    public int getMusic() {
        return music;
    }

    /**
     * @param music music index to set
     */
    public void setMusic(int music) {
        this.music = music;
    }

    /**
     * @return the background url
     */
    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    /**
     * @param backgroundUrl background url to set
     */
    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    /**
     * Updates the object variables from the database
     */
    public void updateLevel(){
        Level newLevel = Main.db.fetchLevelObject(Main.db.fetchLevelId(Main.user.getLobbyKey()));
        if (newLevel != null) {
            this.setMusic(newLevel.getMusic());
            this.setBackgroundUrl(newLevel.getBackgroundUrl());
            this.backgroundImage.setImage(new Image("GUI/images/" + this.getBackgroundUrl()));
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(this.music);
        }
        else {
            this.setLevelId(this.getLevelId() + 1);
        }
    }
}
