package oz.moviematch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import oz.moviematch.models.Movie;
import oz.moviematch.models.ResultsRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieResultsActivity  extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ResultsRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        List<Movie> movies = (List<Movie>)getIntent().getSerializableExtra("MOVIES");
        mRecyclerView = findViewById(R.id.movie_results);
        mAdapter = new ResultsRecyclerViewAdapter(this, movies);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
