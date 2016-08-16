
package elsuper.david.com.spacetravel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MarsRoverResponse implements Serializable {

    @SerializedName("photos")
    private List<Photo> photos = new ArrayList<Photo>();

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
