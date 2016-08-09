
package elsuper.david.com.spacetravel.model;

import com.google.gson.annotations.SerializedName;

public class Camera {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("rover_id")
    private Integer roverId;
    @SerializedName("full_name")
    private String fullName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoverId() {
        return roverId;
    }

    public void setRoverId(Integer roverId) {
        this.roverId = roverId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
