package elsuper.david.com.spacetravel.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Apod implements Serializable {

    @SerializedName("copyright")
    private String copyright;
    @SerializedName("date")
    private String date;
    @SerializedName("explanation")
    private String explanation;
    @SerializedName("hdurl")
    private String hdurl;
    @SerializedName("media_type")
    private String mediaType;
    @SerializedName("service_version")
    private String serviceVersion;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

     public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

     public String getMediaType() {
        return mediaType;
    }

     public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

     public String getServiceVersion() {
        return serviceVersion;
    }

     public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
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
