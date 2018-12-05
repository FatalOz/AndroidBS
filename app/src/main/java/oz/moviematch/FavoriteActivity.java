package oz.moviematch;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import oz.moviematch.models.Movie;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
//    private MovieRecyclerViewAdapter mAdapter;
//    private MovieItemViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

//        mViewModel = ViewModelProvider.of(this).get(MovieItemViewModel.class);

        mRecyclerView = findViewById(R.id.movie_recyclerview);
//        mAdapter = new MovieRecyclerViewAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mViewModel.loadFavorites().observe(this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(@Nullable List<Movie> movies) {
//                mAdapter.setFavorites(movies);
//            }
//        });


    }
}
