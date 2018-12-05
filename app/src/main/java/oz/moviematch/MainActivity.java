package oz.moviematch;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends FragmentActivity {
    // When requested, this adapter returns a ObjectFragment,
    // representing an object in the collection.
    oz.moviematch.CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to AWS
        //AWSMobileClient.getInstance().initialize(this).execute();
        setContentView(R.layout.activity_main);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }
}




