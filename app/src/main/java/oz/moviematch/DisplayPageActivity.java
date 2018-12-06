package oz.moviematch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

import oz.moviematch.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayPageActivity extends Activity {
    TextView movieName, movieYear, likePercentage;
    ImageView likeButton, favoriteButton, moviePoster;
    final String ratingMessage = " liked this movie";
    boolean isLiked = false;
    boolean isFavorited = false;
    int percentLiked = 78;
    int movieId = 0;

    public static final String ARG_OBJECT = "object";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieId = 2231461;

        OmdbInterface myInterface = retrofit.create(OmdbInterface.class);

        movieName = findViewById(R.id.movieName);
        movieYear = findViewById(R.id.movieYear);
        moviePoster = findViewById(R.id.poster);
        likeButton = findViewById(R.id.likeButton);
        likePercentage = findViewById(R.id.likePercentage);
        favoriteButton = findViewById(R.id.favoriteButton);
        myInterface.getMovie("tt" + String.format("%07d", movieId)).enqueue(movieCallback);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = !isLiked;
                if(isLiked){
                    likeButton.setImageResource(R.drawable.thumbs_up_filled);
                    if(percentLiked != 100){
                        percentLiked++;
                    }
                } else {
                    likeButton.setImageResource(R.drawable.thumb_up_empty);
                    if(percentLiked != 0){
                        percentLiked--;
                    }
                }
                likePercentage.setText(percentLiked + "% " + ratingMessage);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorited = !isFavorited;
                if(isFavorited){
                    favoriteButton.setImageResource(R.drawable.ic_star_gold_24dp);

                    // Toast
                    Toast toast = Toast.makeText(getApplicationContext(), "Favorite Added", Toast.LENGTH_SHORT);
                    toast.show();

                    // Add to Favorites List
                    addToFavorites("" + movieId, getBaseContext());

                    // DEBUG
                    ArrayList<String> favorites = getFavorites(getBaseContext());
                    for(String favorite: favorites){
                        Log.d("Favorites", favorite);
                    }

                } else {
                    removeFromFavorites("" + movieId, getBaseContext());
                    favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });


        likePercentage.setText(percentLiked + "% " + ratingMessage);


    }

    Callback<Movie> movieCallback = new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            if (response.isSuccessful()) {
                Movie movieResponse = response.body();
                // Get All Movie Data
                String posterUrl = movieResponse.getPosterUrl();
                String movieTitle = movieResponse.getTitle();
                String movieRelease = movieResponse.getYear();
                // Set All Movie Data
                movieName.setText(movieTitle);
                movieYear.setText(movieRelease);
                Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.ic_movie_filter_black_24dp)
                        .error(R.drawable.ic_movie_filter_black_24dp)
                        .resize(600, 700)
                        .into(moviePoster);
            } else {
                Log.d("DisplayPageActivity", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {
            t.printStackTrace();
        }
    };

    public static void addToFavorites(String value, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        ArrayList<String> list = getFavorites(context);
        list.add(value);
        String json = gson.toJson(list);
        editor.putString("favorites", json);
        editor.apply();
    }

    public static void removeFromFavorites(String value, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        ArrayList<String> list = getFavorites(context);
        list.remove(value);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("favorites", json);
        editor.apply();
    }

    public static ArrayList<String> getFavorites(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString("favorites", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> list = gson.fromJson(json, type);
        if(list == null){
            list = new ArrayList<String>();
        }
        return list;
    }
}
