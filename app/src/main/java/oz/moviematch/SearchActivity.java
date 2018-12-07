package oz.moviematch;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oz.moviematch.models.Movie;
import oz.moviematch.models.MovieList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends ListActivity {
    List<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }
    private void doSearch(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final OmdbInterface myInterface = retrofit.create(OmdbInterface.class);
        myInterface.getMovies(query).enqueue(moviesCallback);
    }
    Callback<MovieList> moviesCallback = new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    MovieList movies = response.body();
                    data = new ArrayList<>();
                    for (Movie movie : movies.getMovies()) {
                        Map<String, String> remap = new HashMap<>();
                        remap.put("Title", movie.getTitle());
                        remap.put("Year", movie.getYear());
                        remap.put("ID", movie.getId());
                        data.add(remap);
                    }
                    ListAdapter adapter = new SimpleAdapter(
                            getBaseContext(),
                            data,
                            R.layout.movie_search_item,
                            new String[] {"Title", "Year"},
                            new int[] {R.id.title, R.id.year});
                    setListAdapter(adapter);
                } else {
                    Log.d("SearchActivity", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                t.printStackTrace();
            }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        String movieId = data.get(position).get("ID");
        Intent intent = new Intent(v.getContext(), DisplayPageActivity.class);
        intent.putExtra("MOVIE_ID", movieId);
        v.getContext().startActivity(intent);
    }
}

