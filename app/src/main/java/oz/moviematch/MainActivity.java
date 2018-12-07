package oz.moviematch;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends FragmentActivity {
    // When requested, this adapter returns a ObjectFragment,
    // representing an object in the collection.
    oz.moviematch.CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    private EditText mSearchBoxEditText;
    private ProgressBar mProgressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to AWS (Comment out to run the app without a server)
        AWSMobileClient.getInstance().initialize(this).execute();
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.ma_search_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mCollectionPagerAdapter =
                new oz.moviematch.CollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
    }
    //search menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
}




