package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by gpailet on 6/26/17.
 */
@Parcel
public class Tweet {
    //list out the attributes
    public String body;
    public long uid; //database ID for the tweet
    public User user;
    public String createdAt;
    //public String imageURL;

    public String reply;
    public int retweet_count;
    public int favorite_count;
    public boolean retweet_status;
    public boolean favorite_status;


    public Tweet(){
    }

    // deserialilze the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet =new Tweet();

        //extract the values from JSON
        tweet.body=jsonObject.getString("text");
        tweet.uid=jsonObject.getLong("id");
        tweet.createdAt=jsonObject.getString("created_at");
        tweet.user=User.fromJSON(jsonObject.getJSONObject("user"));



        //tweet.imageURL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url") + ":large";

        tweet.reply=jsonObject.getString("in_reply_to_status_id");
        tweet.retweet_count=jsonObject.getInt("retweet_count");
        tweet.favorite_count=jsonObject.getInt("favorite_count");
        tweet.retweet_status=jsonObject.getBoolean("retweeted");
        tweet.favorite_status=jsonObject.getBoolean("favorited");
        return tweet;
    }
}
