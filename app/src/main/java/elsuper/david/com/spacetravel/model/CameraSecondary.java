
package elsuper.david.com.spacetravel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CameraSecondary implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("full_name")
    private String fullName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
