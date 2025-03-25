package dto;

import com.google.gson.annotations.SerializedName;

public class Work {

    @SerializedName("key")
    private String id;

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }
}