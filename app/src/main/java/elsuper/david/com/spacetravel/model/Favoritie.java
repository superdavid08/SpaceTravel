package elsuper.david.com.spacetravel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Andrés David García Gómez
 */
public class Favoritie implements Serializable {

    @SerializedName("isApod")
    private Boolean isApod;
    @SerializedName("id")
    private Integer id;
    @SerializedName("date")
    private String date;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;

    public Boolean getIsApod() {
        return isApod;
    }

    public void setIsApod(Boolean isApod) {
        this.isApod = isApod;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}