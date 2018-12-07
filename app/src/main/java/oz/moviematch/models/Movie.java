package oz.moviematch.models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    public String getPosterUrl() {
        return posterUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getYear() {
        return year;
    }
    public String getId() { return id; }
    @SerializedName("Title")
    String title;
    @SerializedName("Year")
    String year;
    @SerializedName("Poster")
    String posterUrl;
    @SerializedName("imdbID")
    String id;

    public Boolean hasPoster(){
        if(posterUrl.contentEquals("N/A")){
            return false;
        }
        else{
            return true;
        }
    }
    public Movie(String title, String year, String posterUrl, String id) {
        this.title = title;
        this.year = year;
        this.posterUrl = posterUrl;
        this.id = id;
    }
}