package oz.moviematch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import oz.moviematch.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    static oz.moviematch.OmdbInterface omdbInterface;
    static boolean isFavorite = false;
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        omdbInterface = retrofit.create(oz.moviematch.OmdbInterface.class);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ObjectFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(ObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

    public static class ObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        View view;
        TextView movieName;
        TextView movieYear;
        ImageView moviePoster;
        ImageView favoriteButton;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            final View rootView = inflater.inflate(
                    R.layout.fragment_collection_object, container, false);
            final Bundle args = getArguments();

            omdbInterface.getMovie("tt" + String.format("%07d", args.getInt(ARG_OBJECT))).enqueue(movieCallback);

            Log.d("DEBUG","tt" + String.format("%07d", args.getInt(ARG_OBJECT)));

            //new RetrieveMovie(this).execute(String.format("%07d", args.getInt(ARG_OBJECT)));

            final RatingBar ratingBar = rootView.findViewById(R.id.ratingBar);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    //TODO: store rating data in user account DB
                    //TODO: <2 second animation before next view
                    if(b){
                        ViewPager pager = rootView.getRootView().findViewById(R.id.pager);
                        pager.setCurrentItem(pager.getCurrentItem()+1, true);
                    }
                }
            });

            view = rootView;
            movieName = view.findViewById(R.id.movieName);
            movieYear = view.findViewById(R.id.movieYear);
            moviePoster = view.findViewById(R.id.poster);

            favoriteButton = view.findViewById(R.id.favoriteButton);
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));

            favoriteButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //View favorites
                    Intent intent = new Intent(view.getContext(), FavoriteActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add to favorites
                    isFavorite = !isFavorite;
                    if (isFavorite) {
                        favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                        Toast.makeText(v.getContext(), "Favorite Deleted", Toast.LENGTH_SHORT).show();
                        DisplayPageActivity.removeFromFavorites("" + args.getInt(ARG_OBJECT), view.getContext());
                    } else {
                        favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gold_24dp));
                        Toast.makeText(v.getContext(), "Favorite Added!", Toast.LENGTH_SHORT).show();
                        DisplayPageActivity.addToFavorites("" + args.getInt(ARG_OBJECT), view.getContext());
                    }

                }
            });
            return rootView;
        }

        Callback<Movie> movieCallback = new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    movieName.setText(movie.getTitle());
                    movieYear.setText(movie.getYear());
                    if(movie.hasPoster()){
                        Picasso.get()
                                .load(movie.getPosterUrl())
                                .into(moviePoster);
                    }
                    else {
                        Drawable res = getResources().getDrawable(R.drawable.confused_travolta);
                        moviePoster.setImageDrawable(res);
                    }
                } else {
                    Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.printStackTrace();
            }
        };

    }
    /* public static class RetrieveMovie extends AsyncTask<String, Void, Movie> {

        static final String API_KEY = "e01b8940";
        static final String API_URL = "http://www.omdbapi.com/?";

        ObjectFragment caller;

        RetrieveMovie(ObjectFragment caller){
            this.caller = caller;
        }

        protected Movie doInBackground(String... params) {
            try {
                URL url = new URL(API_URL + "i=tt" + params[0] + "&apiKey=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Gson gson = new Gson();
                    JsonObject movieJson = new JsonParser().parse(stringBuilder.toString()).getAsJsonObject();
                    JsonElement title = movieJson.get("Title");
                    JsonElement year = movieJson.get("Year");
                    Drawable image;
                    if (!movieJson.get("Poster").getAsString().contentEquals("N/A")){
                        try {
                            InputStream is = (InputStream) new URL(movieJson.get("Poster").getAsString()).getContent();
                            image = Drawable.createFromStream(is, "src name");
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);
                            return new Movie(title.getAsString(), year.getAsString());
                        }
                    }
                    else {
                        return new Movie(title.getAsString(), year.getAsString());
                        //TODO: Actual missing image art
                    }

                    return new Movie(title.getAsString(), year.getAsString(), image);
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(Movie response) {
            caller.onDataLoaded(response);
        }
    } */
}

