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
    @SerializedName("Title")
    String title;
    @SerializedName("Year")
    String year;
    @SerializedName("Poster")
    String posterUrl;

    public Boolean hasPoster(){
        if(posterUrl.contentEquals("N/A")){
            return false;
        }
        else{
            return true;
        }
    }
    public Movie(String title, String year, String posterUrl) {
        this.title = title;
        this.year = year;
        this.posterUrl = posterUrl;
    }
}