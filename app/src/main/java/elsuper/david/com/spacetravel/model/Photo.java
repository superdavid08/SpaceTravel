
package elsuper.david.com.spacetravel.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    private Integer id;
    @SerializedName("sol")
    private Integer sol;
    @SerializedName("camera")
    private Camera camera;
    @SerializedName("img_src")
    private String imgSrc;
    @SerializedName("earth_date")
    private String earthDate;
    @SerializedName("rover")
    private Rover rover;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSol() {
        return sol;
    }

    public void setSol(Integer sol) {
        this.sol = sol;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public Rover getRover() {
        return rover;
    }

    public void setRover(Rover rover) {
        this.rover = rover;
    }
}
