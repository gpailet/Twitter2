package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.ProfileActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TweetDetailsActivity;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gpailet on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {

    public interface TweetSelectedListener{
        // handle tweet selection
        public void onTweetSelected(Tweet tweet);
    }

    public TweetAdapter tweetAdapter;
    public ArrayList<Tweet> tweets;
    public RecyclerView rvTweets;
    public TwitterClient client;
    private final int REQUEST_CODE=20;

    //inflation happens inside onCreateView


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        View v=inflater.inflate(R.layout.fragments_tweets_list,container
        ,false);

        // find the RecyclerView
        rvTweets=(RecyclerView)v.findViewById(R.id.rvTweet);
        // init the arraylist (data source)
        tweets=new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter=new TweetAdapter(tweets,this);
        //RecyclerView setup (layout manager,use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);
        client= TwitterApp.getRestClient();

        return v;
    }

    public TweetAdapter getTweetAdapter() {
        return tweetAdapter;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public RecyclerView getRvTweets() {
        return rvTweets;
    }

    public void addItems(JSONArray response){
        try {
            for (int i=0;i<response.length();i++) {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet=tweets.get(position);
        ((TweetSelectedListener)getActivity()).onTweetSelected(tweet);
        Intent i=new Intent(getActivity(), TweetDetailsActivity.class);
        i.putExtra("detail", Parcels.wrap(tweet));
        startActivity(i);
    }

    @Override
    public void onProfileSelected(View view, int position) {
        Tweet tweet=tweets.get(position);
        Intent i=new Intent(getActivity(), ProfileActivity.class);
        i.putExtra("screen_name",tweet.user.screenName);
        startActivity(i);
    }

    @Override
    public void onLikeSelected(View view, int position) {
        Tweet tweet=tweets.get(position);
        if (tweet.favorite_status){
            client.unFavorite(tweet.uid,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Toast.makeText(getActivity(),"Tweet successfully unfavorited",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
        else{
            client.favorite(tweet.uid,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Toast.makeText(getActivity(),"Tweet successfully favorited",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    @Override
    public void onRetweetSelected(View view, int position) {
        Tweet tweet=tweets.get(position);
    }

    @Override
    public void onReplySelected(View view, int position) {
        Tweet tweet=tweets.get(position);
        Intent i=new Intent(getActivity(), ComposeActivity.class);
        i.putExtra("id",tweet.uid);
        i.putExtra("screen_name",tweet.user.screenName);
        startActivityForResult(i,REQUEST_CODE);
    }
}
