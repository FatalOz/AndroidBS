package oz.moviematch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import oz.moviematch.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> mMovies;

    public MovieRecyclerViewAdapter(Context context, ArrayList<String> movies) {
        mInflater = LayoutInflater.from(context);
        mMovies = movies;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_item, parent, false);
        MovieViewHolder holder = new MovieViewHolder(view, this);
        view.setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String current = mMovies.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        ImageView moviePoster;
        TextView movieName;

        public MovieViewHolder(View view, MovieRecyclerViewAdapter adapter) {
            super(view);
            this.moviePoster = (ImageView)  view.findViewById(R.id.poster);
            this.movieName = (TextView) view.findViewById(R.id.movieName);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DisplayPageActivity.class);
            v.getContext().startActivity(intent);
        }

        public void bind(String current) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OmdbInterface myInterface = retrofit.create(OmdbInterface.class);

            Log.d("DEBUG", "tt" + String.format("%07d", Integer.parseInt(current)));

            myInterface.getMovie("tt" + String.format("%07d", Integer.parseInt(current))).enqueue(movieCallback);

        }

        Callback<Movie> movieCallback = new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movieResponse = response.body();
                    // Get All Movie Data
                    String posterUrl = movieResponse.getPosterUrl();
                    String movieTitle = movieResponse.getTitle();
                    // Set All Movie Data
                    movieName.setText(movieTitle);
                    Picasso.get()
                            .load(posterUrl)
                            .placeholder(R.drawable.ic_movie_filter_black_24dp)
                            .error(R.drawable.ic_movie_filter_black_24dp)
                            .resize(600, 700)
                            .into(moviePoster);
                } else {
                    Log.d("MovieAdapter", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.printStackTrace();
            }
        };
    }
}
