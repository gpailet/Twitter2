package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

/**
 * Created by gpailet on 6/29/17.
 */

public class TweetDetailsActivity extends AppCompatActivity{
    Tweet tweet;
    TextView tvTitleName;
    TextView tvHandle;
    TextView tvDetailBody;
    ImageView ivDetailTweetPicture;
    ImageView ivDetailProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tweet= Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvTitleName=(TextView)findViewById(R.id.tvTitleName);
        tvHandle=(TextView)findViewById(R.id.tvHandle);
        tvDetailBody=(TextView)findViewById(R.id.tvDetailBody);
        ivDetailProfileImage=(ImageView)findViewById(R.id.ivDetailProfileImage);
        ivDetailTweetPicture=(ImageView)findViewById(R.id.ivTweetPicture);

        tvTitleName.setText(tweet.user.name);
        tvHandle.setText("@"+tweet.user.screenName);
        tvDetailBody.setText(tweet.body);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivDetailProfileImage);
    }
}
