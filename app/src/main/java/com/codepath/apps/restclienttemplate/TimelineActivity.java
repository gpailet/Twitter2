package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private final int REQUEST_CODE=20;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    ProgressBar v;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

        populateTimeline();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client=TwitterApp.getRestClient();

        //Lookup the swipe container view
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        //Setup refresh listerner which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                tweetAdapter.clear();
                populateTimeline();
                swipeContainer.setRefreshing(false);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvTweets=(RecyclerView)findViewById(R.id.rvTweet);
        // init the arraylist (data source)
        tweets=new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter=new TweetAdapter(tweets);
        //RecyclerView setup (layout manager,use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);
        //populateTimeline();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet =  Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
            tweets.add(0,tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    private void populateTimeline(){
        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient",response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("TwitterClient",response.toString());
                //iterate through the JSON array
                //for each entry, deserialize the JSON object
                for (int i=0;i<response.length();i++) {
                    //convert each object to a Tweet model
                    //add that Tweet model to our data source
                    // notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient",responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }
        });
        hideProgressBar();
    }
}
