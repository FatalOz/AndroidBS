package oz.moviematch.models;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

@DynamoDBTable(tableName = "Movies")

public class MoviesDO {
    private String _movieId;
    private Map<String, Boolean> _ratings;
    private Map<String, String> _reviews;

    @DynamoDBHashKey(attributeName = "movieId")
    @DynamoDBAttribute(attributeName = "movieId")
    public String getMovieId() {
        return _movieId;
    }

    public void setMovieId(final String _movieId) {
        this._movieId = _movieId;
    }
    @DynamoDBRangeKey(attributeName = "ratings")
    @DynamoDBAttribute(attributeName = "ratings")
    public Map<String, Boolean> getRatings() {
        return _ratings;
    }

    public void setRatings(final Map<String, Boolean> _ratings) {
        this._ratings = _ratings;
    }

    @DynamoDBAttribute(attributeName = "reviews")
    public Map<String, String> getReviews() {
        return _reviews;
    }

    public void setReviews(final Map<String, String> _reviews) {
        this._reviews = _reviews;
    }

}
