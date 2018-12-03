package oz.moviematch.main.moviematch.models;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

@DynamoDBTable(tableName = "moviecritic-mobilehub-1779569470-Movies")

public class MoviesDO {
    private String _userId;
    private Double _popularity;
    private String _imdbId;
    private Map<String, String> _reviews;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "popularity")
    @DynamoDBAttribute(attributeName = "popularity")
    public Double getPopularity() {
        return _popularity;
    }

    public void setPopularity(final Double _popularity) {
        this._popularity = _popularity;
    }
    @DynamoDBIndexHashKey(attributeName = "imdbId", globalSecondaryIndexName = "movie")
    public String getImdbId() {
        return _imdbId;
    }

    public void setImdbId(final String _imdbId) {
        this._imdbId = _imdbId;
    }
    @DynamoDBAttribute(attributeName = "reviews")
    public Map<String, String> getReviews() {
        return _reviews;
    }

    public void setReviews(final Map<String, String> _reviews) {
        this._reviews = _reviews;
    }

}
