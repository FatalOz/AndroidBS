package oz.moviematch.main.moviematch.models;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

@DynamoDBTable(tableName = "moviecritic-mobilehub-1779569470-Profiles")

public class ProfilesDO {
    private String _userId;
    private Double _rep;
    private Map<String, String> _reviews;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "rep")
    @DynamoDBAttribute(attributeName = "rep")
    public Double getRep() {
        return _rep;
    }

    public void setRep(final Double _rep) {
        this._rep = _rep;
    }
    @DynamoDBAttribute(attributeName = "reviews")
    public Map<String, String> getReviews() {
        return _reviews;
    }

    public void setReviews(final Map<String, String> _reviews) {
        this._reviews = _reviews;
    }

}
