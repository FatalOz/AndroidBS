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
    private Map<String, String> _reviews;
    private Double _rating;

    @DynamoDBHashKey(attributeName = "movieId")
    @DynamoDBAttribute(attributeName = "movieId")
    public String getMovieId() {
        return _movieId;
    }

    @DynamoDBAttribute(attributeName = "reviews")
    public Map<String, String> getReviews() {
        return _reviews;
    }

    public void setReviews(final Map<String, String> _reviews) {
        this._reviews = _reviews;
    }

    @DynamoDBAttribute(attributeName = "rating")
    public Double getRating(){ return _rating; }
}
