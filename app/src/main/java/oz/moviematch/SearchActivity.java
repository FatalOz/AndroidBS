package oz.moviematch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import java.util.List;

import oz.moviematch.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends Activity {
    Button searchButton;
    EditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        textInput = findViewById(R.id.search_box);
        searchButton=findViewById(R.id.searchButton);
        final OmdbInterface myInterface = retrofit.create(OmdbInterface.class);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("textBox", textInput.getText().toString());
                myInterface.getMovies(textInput.getText().toString()).enqueue(moviesCallback);
            }

        });
    }
    Callback<List<Movie>> moviesCallback = new Callback<List<Movie>>() {
        @Override
        public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
            if (response.isSuccessful()) {
                List<Movie> movieResponses = response.body();
                // Populate RecyclerView

            } else {
                Log.d("DisplayPageActivity", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<List<Movie>> call, Throwable t) {
            t.printStackTrace();
        }
    };


}
