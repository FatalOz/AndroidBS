package oz.moviematch.models;

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
import java.util.List;

import oz.moviematch.DisplayPageActivity;
import oz.moviematch.OmdbInterface;
import oz.moviematch.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.MovieViewHolder> {

    private LayoutInflater mInflater;
    private List<Movie> mMovies;

    public ResultsRecyclerViewAdapter(Context context, List<Movie> movies) {
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
        Movie current = mMovies.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView movieName;
        String movieId;

        public MovieViewHolder(View view, ResultsRecyclerViewAdapter adapter) {
            super(view);
            this.movieName = (TextView) view.findViewById(R.id.movieNameSearchItem);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DisplayPageActivity.class);
            intent.putExtra("MOVIE_ID", movieId);
            v.getContext().startActivity(intent);
        }

        public void bind(Movie current) {
            this.movieName.setText(current.getTitle());
            this.movieId = current.id.substring(2);
        }
    }
}
