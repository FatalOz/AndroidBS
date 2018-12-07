package oz.moviematch.models;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

@DynamoDBTable(tableName = "moviecritic-mobilehub-1779569470-Profiles")

public class ProfilesDO {
    private String _userId;
    private Map<String,Boolean> _ratings;
    private Map<String, String> _reviews;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "ratings")
    @DynamoDBAttribute(attributeName = "ratings")
    public Map<String,Boolean> getRatings() {
        return _ratings;
    }

    public void setRatings(final Map<String,Boolean> _ratings) {
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
