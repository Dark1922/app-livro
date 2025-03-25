package dto;

import com.google.gson.annotations.SerializedName;
import java.util.*;

public class OpenLibraryResponse {

    private String title;
    private List<Object> authors = Collections.emptyList();
    @SerializedName("publish_date") private String publishDate;
    private List<String> publishers = Collections.emptyList();

    public String getTitle() {
        return title != null ? title : "";
    }

    public List<Object> getAuthors() {
        return authors != null ? authors : Collections.emptyList();
    }

    public String getPublishDate() {
        return publishDate != null ? publishDate : "";
    }

    public List<String> getPublishers() {
        return publishers != null ? publishers : Collections.emptyList();
    }
}