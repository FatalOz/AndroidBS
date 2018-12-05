package oz.moviematch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayPageActivity extends Activity {
    TextView movieName, movieYear, likePercentage;
    ImageView likeButton, favoriteButton, moviePoster;
    final String ratingMessage = " liked this movie";
    boolean isLiked = false;
    boolean isFavorited = false;
    int percentLiked = 78;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.omdbapi.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        OmdbInterface myInterface = retrofit.create(OmdbInterface.class);

//        myInterface.getMovie("tt" + String.format("%07d", args.getInt(ARG_OBJECT))).enqueue(movieCallback);
        movieName = findViewById(R.id.movieName);
        movieYear = findViewById(R.id.movieYear);
        moviePoster = findViewById(R.id.poster);
        likeButton = findViewById(R.id.likeButton);
        likePercentage = findViewById(R.id.likePercentage);
        favoriteButton = findViewById(R.id.favoriteButton);


        // Get All Movie Data
        String posterUrl = "https://images-na.ssl-images-amazon.com/images/I/71P30QVnE3L._AC_UL320_SR214,320_.jpg";
        String movie = "Star Wars";
        // Set All Movie Data
        movieName.setText(movie);
        movieYear.setText("2016");
        Picasso.get()
                .load(posterUrl)
                .placeholder(R.drawable.ic_movie_filter_black_24dp)
                .error(R.drawable.ic_movie_filter_black_24dp)
                .resize(600, 700)
                .into(moviePoster);

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
                    Toast toast = Toast.makeText(getApplicationContext(), "Favorite Added", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });


        likePercentage.setText(percentLiked + "% " + ratingMessage);
    }
}
