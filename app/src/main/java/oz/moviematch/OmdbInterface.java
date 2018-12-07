package oz.moviematch;

import java.util.List;

import oz.moviematch.models.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbInterface {
    @GET("?apiKey=e01b8940")
    Call<Movie> getMovie(@Query("i") String imdbId);

    @GET("?apiKey=e01b8940")
    Call<List<Movie>> getMovies(@Query("s") String name);
}
