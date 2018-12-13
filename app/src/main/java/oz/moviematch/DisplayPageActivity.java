package oz.moviematch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oz.moviematch.models.Movie;
import oz.moviematch.models.MoviesDO;
import oz.moviematch.models.ProfilesDO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayPageActivity extends AppCompatActivity {
    TextView movieName, movieYear, likePercentage, movieSuggestion1, movieSuggestion2, movieSuggestion3;
    ImageView likeButton, dislikeButton, moviePoster, movieSuggestionPoster1, movieSuggestionPoster2, movieSuggestionPoster3;
    private Menu menu;
    final String ratingMessage = "liked this movie!";
    boolean isLiked = false;
    boolean isNotLiked = false;
    boolean isFavorited = false;

    int percentLiked = 50;
    Map<String, Boolean> ratings;

    String movieId;

    public static final String ARG_OBJECT = "object";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_display);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieId = getIntent().getStringExtra("MOVIE_ID");


        final OmdbInterface myInterface = retrofit.create(OmdbInterface.class);

        final ArrayList<String> favorites = getFavorites(getBaseContext());

        movieName = findViewById(R.id.movieName);
        movieYear = findViewById(R.id.movieYear);
        moviePoster = findViewById(R.id.poster);
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);
        likePercentage = findViewById(R.id.likePercentage);

        movieSuggestion1 = findViewById(R.id.movieSuggestion1);
        movieSuggestion2 = findViewById(R.id.movieSuggestion2);
        movieSuggestion3 = findViewById(R.id.movieSuggestion3);
        movieSuggestionPoster1 = findViewById(R.id.movieSuggestionPoster1);
        movieSuggestionPoster2 = findViewById(R.id.movieSuggestionPoster2);
        movieSuggestionPoster3 = findViewById(R.id.movieSuggestionPoster3);
        myInterface.getMovie(movieId).enqueue(movieCallback);


        final Handler uiHandler = new Handler(getBaseContext().getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                //add to movie table
                MoviesDO movie = DBUtils.readMovie(movieId);

                ratings = movie.getRatings();
                Boolean rating = ratings.get(DBUtils.getUserId());
                if(rating != null){
                    if(rating){
                        isLiked = true;
                    } else{
                        isNotLiked = true;
                    }
                }
                String[] movies = getThreeRelevantMovies(ratings, movieId);



                if(movies[0] != null){
                    myInterface.getMovie(movies[0]).enqueue(movieSuggestion1Callback);
                }

                if (movies[1] != null){
                    myInterface.getMovie(movies[1]).enqueue(movieSuggestion2Callback);

                }

                if(movies[2] != null){
                    myInterface.getMovie(movies[2]).enqueue(movieSuggestion3Callback);

                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run () {
                        // make operation on UI - on example
                        // on progress bar.
                        likePercentage.setText(percentLiked + "% " + ratingMessage);

                        if(isLiked){
                            likeButton.setImageResource(R.drawable.thumbs_up_filled);
                        } else {
                            likeButton.setImageResource(R.drawable.thumb_up_empty);
                        }

                        if(isNotLiked){
                            dislikeButton.setImageResource(R.drawable.thumbs_up_filled);
                        } else {
                            dislikeButton.setImageResource(R.drawable.thumb_up_empty);
                        }

                        likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isLiked = !isLiked;
                                if(isLiked){
                                    DBUtils.updateRating(movieId, true);
                                    likeButton.setImageResource(R.drawable.thumbs_up_filled);
                                    dislikeButton.setImageResource(R.drawable.thumb_up_empty);

                                } else {
                                    likeButton.setImageResource(R.drawable.thumb_up_empty);

                                    DBUtils.updateRating(movieId, null);
                                    isNotLiked = false;

                                }
                                likePercentage.setText(percentLiked + "% " + ratingMessage);
                            }
                        });

                        dislikeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isNotLiked = !isNotLiked;
                                if(isNotLiked){
                                    DBUtils.updateRating(movieId, false);
                                    dislikeButton.setImageResource(R.drawable.thumbs_up_filled);
                                    isLiked = false;
                                    likeButton.setImageResource(R.drawable.thumb_up_empty);

                                } else {
                                    dislikeButton.setImageResource(R.drawable.thumb_up_empty);
                                    DBUtils.updateRating(movieId, null);

                                }
                                likePercentage.setText(percentLiked + "% " + ratingMessage);
                            }
                        });

                    }
                });

                percentLiked = computeRatings(ratings);

            }

        }).start();
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
                int maxLen = 18;
                String newTitle = movieTitle;
                while(newTitle.length() > maxLen){
                    int lastIndexOfSpace = newTitle.lastIndexOf(' ');
                    if(lastIndexOfSpace == -1){
                       newTitle = newTitle.substring(0, maxLen);
                       break;
                    }

                    newTitle = newTitle.substring(0, lastIndexOfSpace);
                }

                // Banned List
                String[] bannedEndings = {"a", "the", "and", "of", "in", "an", "with", "at", "from", "until", "for", "by"};
                List<String> bannedList = Arrays.asList(bannedEndings);
                int lastIndexOfSpace = newTitle.lastIndexOf(' ');
                while(newTitle.length() - lastIndexOfSpace <= 4){
                    if(bannedList.contains(newTitle.substring(lastIndexOfSpace + 1, newTitle.length()))){
                        newTitle = newTitle.substring(0, lastIndexOfSpace);
                        lastIndexOfSpace = newTitle.lastIndexOf(' ');
                    } else {
                        break;
                    }

                }

//                int max = movieTitle.length() > 20 ? 20 : movieTitle.length();
//                String newTitle = movieTitle.substring(0, max);
//                int lastSpace = newTitle.lastIndexOf(' ');

                setTitle(newTitle + " - " + movieRelease);
                movieName.setText(movieTitle);
//                movieYear.setText(movieRelease);
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

    Callback<Movie> movieSuggestion1Callback = new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            if (response.isSuccessful()) {
                final Movie movieResponse = response.body();
                // Get All Movie Data
                String posterUrl = movieResponse.getPosterUrl();
                String movieTitle = movieResponse.getTitle();
                if(movieTitle.length() > 15){
                    movieTitle = movieTitle.substring(0, 12) + "...";
                }
                // Set All Movie Data
                movieSuggestion1.setText(movieTitle);
                Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.ic_movie_filter_black_24dp)
                        .error(R.drawable.ic_movie_filter_black_24dp)
                        .resize(600, 700)
                        .into(movieSuggestionPoster1);
                movieSuggestionPoster1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayPageActivity.class);
                        intent.putExtra("MOVIE_ID", movieResponse.getId());
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                Log.d("DisplayPageActivity", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {
            t.printStackTrace();
        }
    };

    Callback<Movie> movieSuggestion2Callback = new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            if (response.isSuccessful()) {
                final Movie movieResponse = response.body();
                // Get All Movie Data
                String posterUrl = movieResponse.getPosterUrl();
                String movieTitle = movieResponse.getTitle();
                if(movieTitle.length() > 15){
                    movieTitle = movieTitle.substring(0, 12) + "...";
                }
                // Set All Movie Data
                movieSuggestion2.setText(movieTitle);
                Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.ic_movie_filter_black_24dp)
                        .error(R.drawable.ic_movie_filter_black_24dp)
                        .resize(600, 700)
                        .into(movieSuggestionPoster2);
                movieSuggestionPoster2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayPageActivity.class);
                        intent.putExtra("MOVIE_ID", movieResponse.getId());
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                Log.d("DisplayPageActivity", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {
            t.printStackTrace();
        }
    };

    Callback<Movie> movieSuggestion3Callback = new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            if (response.isSuccessful()) {
                final Movie movieResponse = response.body();
                // Get All Movie Data
                String posterUrl = movieResponse.getPosterUrl();
                String movieTitle = movieResponse.getTitle();
                if(movieTitle.length() > 15){
                    movieTitle = movieTitle.substring(0, 12) + "...";
                }
                // Set All Movie Data
                movieSuggestion3.setText(movieTitle);
                Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.ic_movie_filter_black_24dp)
                        .error(R.drawable.ic_movie_filter_black_24dp)
                        .resize(600, 700)
                        .into(movieSuggestionPoster3);

                movieSuggestionPoster3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayPageActivity.class);
                        intent.putExtra("MOVIE_ID", movieResponse.getId());
                        v.getContext().startActivity(intent);
                    }
                });
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

    public static int computeRatings(Map<String, Boolean> ratings){
        int x = 0;
        Collection<Boolean> ratingCol = ratings.values();
        for(Boolean rating: ratingCol){
            x += rating ? 1 : 0;
        }

        if(ratingCol.size() == 0){
            return 0;
        }

        return (x*100)/ratingCol.size() ;
    }

    // Call in a thread
    public static String[] getThreeRelevantMovies(final Map<String, Boolean> ratings, String movieId){
        String[] movies = new String[3];

        final Hashtable<String, Integer> movieReviews = new Hashtable<>();

        for(String name : ratings.keySet()){
            if(!name.equals(DBUtils.getUserId())){
                ProfilesDO profile = DBUtils.readProfile(name);
                for(String movie : profile.getRatings().keySet()){
                    if(!movie.equals(movieId)){
                        int reviewCount = movieReviews.containsKey(movie) ? movieReviews.get(movie) + 1 : 1;
                        movieReviews.put(movie, reviewCount);
                    }

                }
            }
        }

        Set<String> movieKeys = movieReviews.keySet();

        int[] topMovieRatings = new int[3];
        int lowest = 0;

        for(String movie : movieKeys){
            int movieRating = movieReviews.get(movie);
            if(movieRating > topMovieRatings[lowest]){
                movies[lowest] = movie;
                topMovieRatings[lowest] = movieRating;
                for(int i = 0; i < topMovieRatings.length; i++){
                    if(topMovieRatings[lowest] > topMovieRatings[i]){
                        lowest = i;
                    }
                }
            }
        }
        return movies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_display, menu);
        this.menu = menu;
        final ArrayList<String> favorites = getFavorites(getBaseContext());

        if(favorites.contains(movieId)){
            isFavorited = true;
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_gold_24dp, null));
//            favoriteButton.setIcon(R.drawable.ic_star_gold_24dp);
        } else {
//            favoriteButton.setIcon(R.drawable.ic_star_border_black_24dp);
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_border_black_24dp, null));

        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.favoriteButton){
            isFavorited = !isFavorited;
            if(isFavorited){
                this.menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_gold_24dp, null));

                // Toast
                Toast toast = Toast.makeText(getApplicationContext(), "Favorite Added", Toast.LENGTH_SHORT);
                toast.show();

                // Add to Favorites List
                addToFavorites(movieId, getBaseContext());

            } else {
                removeFromFavorites(movieId, getBaseContext());
                this.menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_border_black_24dp, null));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
