package oz.moviematch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ProgressBar;


public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieRecyclerViewAdapter mAdapter;

    private EditText mSearchBoxEditText;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mSearchBoxEditText = (EditText) findViewById(R.id.ma_search_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        mRecyclerView = findViewById(R.id.movie_recyclerview);
        mAdapter = new MovieRecyclerViewAdapter(this, DisplayPageActivity.getFavorites(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    //search menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
}