package com.example.music_player.Model;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Theme implements Serializable {

    @SerializedName("IdTheme")
    @Expose
    private String idTheme;
    @SerializedName("ThemeName")
    @Expose
    private String themeName;
    @SerializedName("ThemePic")
    @Expose
    private String themePic;
    @SerializedName("IdSong")
    @Expose
    private String idSong;

    public String getIdTheme() {
    return idTheme;
    }

    public void setIdTheme(String idTheme) {
    this.idTheme = idTheme;
    }

    public String getThemeName() {
    return themeName;
    }

    public void setThemeName(String themeName) {
    this.themeName = themeName;
    }

    public String getThemePic() {
    return themePic;
    }

    public void setThemePic(String themePic) {
    this.themePic = themePic;
    }

    public String getIdSong() {
    return idSong;
    }

    public void setIdSong(String idSong) {
    this.idSong = idSong;
    }

}