package oz.moviematch;

import android.content.Context;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.Map;

import oz.moviematch.models.MoviesDO;
import oz.moviematch.models.ProfilesDO;

public class DBUtils {
    static CognitoUserPool userPool;
    private static String userId;
    private static DynamoDBMapper dynamoDBMapper;
    public DBUtils(DynamoDBMapper map, Context context){
        userPool = new CognitoUserPool(context, new AWSConfiguration(context));
        userId = userPool.getCurrentUser().getUserId();
        dynamoDBMapper = map;
    }

    public static void addRating(final String movieId, final Boolean rating){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //add to movie table
                MoviesDO movie = readMovie(movieId);

                Map<String, Boolean> oldRatings = movie.getRatings();
                oldRatings.put(userId, rating);
                movie.setRatings(oldRatings);

                dynamoDBMapper.save(movie);

                //add to profile table
                ProfilesDO profile = readProfile(userId);

                oldRatings = profile.getRatings();
                oldRatings.put(movieId, rating);
                profile.setRatings(oldRatings);

                dynamoDBMapper.save(profile);
            }
        }).start();
    }

    public static MoviesDO readMovie(final String movieId) {
        MoviesDO movie = dynamoDBMapper.load(
                MoviesDO.class,
                movieId);

        return movie;
    }

    public static ProfilesDO readProfile(final String profileId){
        ProfilesDO profile = dynamoDBMapper.load(
                ProfilesDO.class,
                profileId);

        return profile;
    }
}
