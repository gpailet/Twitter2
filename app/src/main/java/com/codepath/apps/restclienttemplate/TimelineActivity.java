package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {
//    private TwitterClient client;

    private final int REQUEST_CODE=20;

    SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    ProgressBar v;
    TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweetsPagerAdapter=new TweetsPagerAdapter(getSupportFragmentManager(),this);

        //get the view pager
        ViewPager vpPager=(ViewPager)findViewById(R.id.viewpager);
        //set the adapter for the pager
        vpPager.setAdapter(tweetsPagerAdapter);
        //setup the TabLayout
        TabLayout tabLayout=(TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);


        //populateTimeline();
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }



    public void onComposeAction(MenuItem mi){
        Intent intent=new Intent(TimelineActivity.this,ComposeActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet =  Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
            tweetsPagerAdapter.getItem(0).getTweets().add(0,tweet);
            tweetsPagerAdapter.getItem(0).getTweetAdapter().notifyItemInserted(0);
            tweetsPagerAdapter.getItem(0).getRvTweets().scrollToPosition(0);

            tweetsPagerAdapter.getItem(1).getTweets().add(0,tweet);
            tweetsPagerAdapter.getItem(1).getTweetAdapter().notifyItemInserted(0);
            tweetsPagerAdapter.getItem(1).getRvTweets().scrollToPosition(0);
        }
    }

    public void onProfileView(MenuItem item) {
        // launch the profile view
        Intent i=new Intent(this,ProfileActivity.class);
        startActivity(i);
    }


    @Override
    public void onTweetSelected(Tweet tweet) {
        Toast.makeText(this,tweet.body,Toast.LENGTH_SHORT).show();
    }

}
